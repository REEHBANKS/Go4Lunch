package com.metanoiasystem.go4lunchxoc.data.repository;

import android.location.Location;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.AllTheListRestaurantsResponse;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.RestaurantResponse;
import com.metanoiasystem.go4lunchxoc.data.network.RestaurantService;
import com.metanoiasystem.go4lunchxoc.data.network.RetrofitClient;

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
    private final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();
    private final CollectionReference restaurantsCollection = FirebaseFirestore.getInstance().collection("restaurants");
    private Disposable restaurantDisposable;

    public LiveData<List<Restaurant>> getRestaurantLiveData() {
        return result;
    }

    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    public void fetchRestaurant(Double latitude, Double longitude, RestaurantFetchCallback callback) {

        fetchFromNetwork(latitude, longitude, callback);
    }

    private void fetchFromNetwork(Double latitude, Double longitude, RestaurantFetchCallback callback) {
        restaurantDisposable = streamFetchRestaurantResponse(latitude, longitude)
                .subscribeWith(new DisposableObserver<List<Restaurant>>() {
                    @Override
                    public void onNext(@NonNull List<Restaurant> restaurants) {
                        // Avant d'ajouter les nouveaux restaurants, supprimez les anciens de Firestore
                        clearRestaurantsFromFirestore().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (Restaurant restaurant : restaurants) {
                                    addRestaurantToFirestore(restaurant);
                                }
                                result.setValue(restaurants);
                                callback.onSuccess(restaurants);
                            } else {
                                Log.e("Firestore", "Erreur lors de la suppression des restaurants", task.getException());
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


    // TEST LIST ADD RESTAURANT FIRESTORE

    private void addRestaurantToFirestore(Restaurant restaurant) {
        restaurantsCollection.document(restaurant.getId()).set(restaurant)
                .addOnSuccessListener(aVoid -> {
                    // Once the restaurant is added, fetch and log the names
                    fetchAndLogRestaurants();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding restaurant", e);
                });
    }

    public void fetchAndLogRestaurants() {
        restaurantsCollection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder table = new StringBuilder("| Restaurant Names |\n");
                        table.append("|------------------|\n");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Raw Document: " + document.getData());
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            Log.d("Firestore", "Mapped Restaurant: " + restaurant.toString());
                            table.append("| ").append(restaurant.getRestaurantName()).append(" |\n");
                        }

                        Log.d("Firestore", table.toString());
                    } else {
                        Log.e("Firestore", "Error fetching restaurants", task.getException());
                    }
                });
    }


    // Function that makes a request to the RestaurantService to fetch all restaurants.
    // It then maps the response to a list of Restaurant objects.
    public Observable<List<Restaurant>> streamFetchRestaurantResponse(Double latitude, Double longitude) {

        String lat = Double.toString(latitude);
        String lng = Double.toString(longitude);
        String location = lat + "," + lng;


        RestaurantService restaurantService = RetrofitClient.getRetrofit().create(RestaurantService.class);
        return restaurantService.getAllRestaurantsResponse(BuildConfig.RR_KEY, location)

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

    public interface RestaurantFetchCallback {
        void onSuccess(List<Restaurant> restaurants);
        void onError(Throwable error);
    }

}
