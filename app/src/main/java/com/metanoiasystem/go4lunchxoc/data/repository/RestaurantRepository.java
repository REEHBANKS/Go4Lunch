package com.metanoiasystem.go4lunchxoc.data.repository;

import android.location.Location;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.AllTheListRestaurantsResponse;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.RestaurantResponse;
import com.metanoiasystem.go4lunchxoc.data.network.RestaurantService;
import com.metanoiasystem.go4lunchxoc.data.network.RetrofitClient;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchAllRestaurantFetchCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchOneRestaurantCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RestaurantRepository {


    private static volatile  RestaurantRepository restaurantRepository;
    private final CollectionReference restaurantsCollection = FirebaseFirestore.getInstance().collection("restaurants");
    private Disposable restaurantDisposable;
    private Disposable oneRestaurantDisposable;
    private boolean apiCalledDuringSession = false; // TODO: Ajouté pour vérifier si l'API a été appelée


    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    public void fetchRestaurant(Double latitude, Double longitude, RepositoryFetchAllRestaurantFetchCallback callback) {
        if (apiCalledDuringSession) {

            getAllRestaurantsFromFirebase();
        } else {
            fetchFromNetwork(latitude, longitude, callback);
        }
    }

    public Task<QuerySnapshot> getAllRestaurantsFromFirebase() {
        return restaurantsCollection.get();
    }


    private void fetchFromNetwork(Double latitude, Double longitude, RepositoryFetchAllRestaurantFetchCallback callback) {
        restaurantDisposable = streamFetchRestaurantResponse(latitude, longitude)
                .doFinally(this::dispose) // TODO dispose à la fin de la chaîne d'opération
                .subscribeWith(new DisposableObserver<List<Restaurant>>() {
                    @Override
                    public void onNext(@NonNull List<Restaurant> restaurants) {
                        // Avant d'ajouter les nouveaux restaurants, supprimez les anciens de Firestore
                        clearRestaurantsFromFirestore().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                apiCalledDuringSession = true; // Mise à jour du drapeau une fois que les données ont été récupérées avec succès

                                // Ajout des nouveaux restaurants à Firestore
                                addRestaurantsToFirestore(restaurants).addOnCompleteListener(additionTask -> {
                                    if (additionTask.isSuccessful()) {
                                        // TODO TO DELETE result.setValue(restaurants);
                                        // Log.d("API_CALLING", "Nombre de restaurants reçus : " + restaurants.size());
                                        callback.onSuccess(restaurants);
                                    } else {
                                        callback.onError(additionTask.getException());
                                    }
                                });
                            } else {
                                callback.onError(task.getException());
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        // Vous pouvez ajouter des logs ou d'autres opérations si nécessaire
                    }
                });
    }

    public void dispose() {
        if (restaurantDisposable != null && !restaurantDisposable.isDisposed()) {
            restaurantDisposable.dispose();
        }
    }

    private Task<Void> addRestaurantsToFirestore(List<Restaurant> restaurants) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (Restaurant restaurant : restaurants) {
            DocumentReference docRef = restaurantsCollection.document(); // Création d'un ID de document unique
            batch.set(docRef, restaurant); // Ajout du restaurant au batch
        }

        return batch.commit(); // Commit le batch pour ajouter tous les restaurants à Firestore
    }




    private Task<Void> clearRestaurantsFromFirestore() {
        // Utilisez une liste pour stocker toutes les tâches de suppression
        List<Task<Void>> deleteTasks = new ArrayList<>();

        // Récupérez tous les documents et ajoutez leurs tâches de suppression à la liste
        return restaurantsCollection.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    // Ne supprimez pas les documents "favoris" et "selected"
                    if (!docId.equals("favorites") && !docId.equals("selected")) {
                        deleteTasks.add(document.getReference().delete());
                    }
                }
            }
            // Attendez que toutes les tâches de suppression soient terminées
            return Tasks.whenAll(deleteTasks);
        });
    }




    // Function that makes a request to the RestaurantService to fetch all restaurants.
    // It then maps the response to a list of Restaurant objects.
    public Observable<List<Restaurant>> streamFetchRestaurantResponse(Double latitude, Double longitude) {

        String lat = Double.toString(latitude);
        String lng = Double.toString(longitude);
        String location = lat + "," + lng;


        RestaurantService restaurantService = RetrofitClient.getRetrofit().create(RestaurantService.class);
        return restaurantService.getAllRestaurantsResponse(
                        BuildConfig.RR_KEY,
                        location,
                        "place_id,name,vicinity,rating,geometry,photos,opening_hours")


                .map((Function<AllTheListRestaurantsResponse, List<Restaurant>>) resultsResponse -> {
                    ArrayList<Restaurant> restaurants = new ArrayList<>();



                    for (RestaurantResponse restaurantResponse : resultsResponse.getResults()) {
                        Boolean isOpen = restaurantResponse.getOpeningResponse() != null ? restaurantResponse.getOpeningResponse().getOpen_now() : false;
                        String photoIsHere = restaurantResponse.getPhotosResponse() == null || restaurantResponse.getPhotosResponse().isEmpty() ? null :
                                restaurantResponse.getPhotosResponse().get(0).getPhotoReference();

                        float[] results = new float[1];
                        Location.distanceBetween(latitude, longitude, restaurantResponse.getGeometryResponse().getLocationResponse().getLat()
                                , restaurantResponse.getGeometryResponse().getLocationResponse().getLng(), results);
                        float distanceResults = results[0];
                        int distance = (int) distanceResults;


                        Restaurant restaurant = new Restaurant(restaurantResponse.getPlace_id(),
                                restaurantResponse.getName(),
                                restaurantResponse.getGeometryResponse().getLocationResponse().getLat(),
                                restaurantResponse.getGeometryResponse().getLocationResponse().getLng(),
                                photoIsHere,
                                restaurantResponse.getVicinity(),
                                isOpen,
                                restaurantResponse.getRating(),
                                distance);

                        restaurants.add(restaurant);
                    }

                    return restaurants;

                })


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void fetchOneRestaurantFromNetwork(LatLng latLng, String id,Float rating, RepositoryFetchOneRestaurantCallback callback) {
        if (oneRestaurantDisposable != null && !oneRestaurantDisposable.isDisposed()) {
            oneRestaurantDisposable.dispose();
        }

        // Start a new fetch operation.


        oneRestaurantDisposable = streamFetchOneRestaurantResponse(latLng, id, rating)
                .subscribeWith(new DisposableObserver<Restaurant>() {

                    @Override
                    public void onNext(@NonNull Restaurant restaurant) {
                        callback.onSuccess(restaurant);
                        Log.d("DEBUG", "Restaurant fetched successfully");


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(e);

                    }

                    @Override
                    public void onComplete() {
                        Log.d("JUSTIN", "On complete");

                    }
                });

    }

          public Observable<Restaurant> streamFetchOneRestaurantResponse(LatLng latLng, String id, Float rating){


            RestaurantService restaurantService = RetrofitClient.getRetrofit().create(RestaurantService.class);
            return restaurantService.getOneRestaurantByIdResponse(BuildConfig.RR_KEY, id)
                    .map(resultOneResponse -> {


                        Boolean isOpen = resultOneResponse.getResult().getOpeningResponse() != null ? resultOneResponse.getResult().getOpeningResponse().getOpen_now() : false;
                        String photoIsHere = resultOneResponse.getResult().getPhotosResponse() == null || resultOneResponse.getResult().getPhotosResponse().isEmpty() ? null :
                                resultOneResponse.getResult().getPhotosResponse().get(0).getPhotoReference();


                        return new Restaurant(resultOneResponse.getResult().getPlace_id(),
                                resultOneResponse.getResult().getName(),
                                resultOneResponse.getResult().getGeometryResponse().getLocationResponse().getLat(),
                                resultOneResponse.getResult().getGeometryResponse().getLocationResponse().getLng(),
                                photoIsHere,
                                resultOneResponse.getResult().getVicinity(),
                                isOpen,
                                rating,
                                0,
                                resultOneResponse.getResult().getFormatted_phone_number(),
                                resultOneResponse.getResult().getWebsite());


                    })

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


        }


    }
