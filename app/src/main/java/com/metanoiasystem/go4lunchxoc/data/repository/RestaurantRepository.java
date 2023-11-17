package com.metanoiasystem.go4lunchxoc.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.metanoiasystem.go4lunchxoc.utils.MyApp;
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

    // Singleton instance and Firestore collection reference for restaurants.
    private static volatile  RestaurantRepository restaurantRepository;
    private final CollectionReference restaurantsCollection = FirebaseFirestore.getInstance().collection("restaurants");
    private Disposable restaurantDisposable;
    private Disposable oneRestaurantDisposable;
    private boolean apiCalledDuringSession = false;

    // Singleton pattern to get the instance of RestaurantRepository.
    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            synchronized (RestaurantRepository.class) {
                if (restaurantRepository == null) {
                    restaurantRepository = new RestaurantRepository();
                }
            }
        }
        return restaurantRepository;
    }

    // Fetches all restaurants from Firestore.
    public Task<QuerySnapshot> getAllRestaurantsFromFirebase() {
        return restaurantsCollection.get();
    }
    // Fetches restaurants based on location and updates them in Firestore.
    public void fetchRestaurant(double latitude, double longitude, RepositoryFetchAllRestaurantFetchCallback callback) {
        SharedPreferences sharedPreferences = MyApp.getAppContext().getSharedPreferences("location_prefs", Context.MODE_PRIVATE);
        double storedLatitude = sharedPreferences.getFloat("latitude", Float.MIN_VALUE);
        double storedLongitude = sharedPreferences.getFloat("longitude", Float.MIN_VALUE);

        if (apiCalledDuringSession || (latitude == storedLatitude && longitude == storedLongitude)) {
            getAllRestaurantsFromFirebase();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("latitude", (float) latitude);
            editor.putFloat("longitude", (float) longitude);
            editor.apply();

            fetchFromNetwork(latitude, longitude, callback);
        }
    }

    // Internal method to fetch restaurant data from the network.
    private void fetchFromNetwork(double latitude, double longitude, RepositoryFetchAllRestaurantFetchCallback callback) {
        // Implementation for fetching and handling restaurant data.
        restaurantDisposable = streamFetchRestaurantResponse(latitude, longitude)
                .doFinally(this::dispose)
                .subscribeWith(new DisposableObserver<List<Restaurant>>() {
                    @Override
                    public void onNext(@NonNull List<Restaurant> restaurants) {
                        clearRestaurantsFromFirestore().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                apiCalledDuringSession = true;

                                // Ajout des nouveaux restaurants à Firestore
                                addRestaurantsToFirestore(restaurants).addOnCompleteListener(additionTask -> {
                                    if (additionTask.isSuccessful()) {
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
                    }
                });
    }
    // Disposes the current restaurantDisposable if not already disposed.
    public void dispose() {
        // Implementation for disposing the disposable.
        if (restaurantDisposable != null && !restaurantDisposable.isDisposed()) {
            restaurantDisposable.dispose();
        }
    }
    // Adds a list of restaurants to Firestore in a batch operation.
    private Task<Void> addRestaurantsToFirestore(List<Restaurant> restaurants) {
        // Implementation for adding restaurants to Firestore.
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (Restaurant restaurant : restaurants) {
            DocumentReference docRef = restaurantsCollection.document();
            batch.set(docRef, restaurant);
        }
        return batch.commit();
    }

    // Clears restaurants from Firestore, except "favorites" and "selected" documents.
    private Task<Void> clearRestaurantsFromFirestore() {
        // Implementation for clearing restaurants from Firestore.
        List<Task<Void>> deleteTasks = new ArrayList<>();
        return restaurantsCollection.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    if (!docId.equals("favorites") && !docId.equals("selected")) {
                        deleteTasks.add(document.getReference().delete());
                    }
                }
            }
            return Tasks.whenAll(deleteTasks);
        });
    }

    // Streams restaurant responses from the network based on location.
    public Observable<List<Restaurant>> streamFetchRestaurantResponse(double latitude, double longitude) {
        // Implementation for streaming restaurant responses.
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
    // Fetches details of a single restaurant from the network.
    public void fetchOneRestaurantFromNetwork(LatLng latLng, String id,Float rating, RepositoryFetchOneRestaurantCallback callback) {
        // Implementation for fetching a single restaurant.
        if (oneRestaurantDisposable != null && !oneRestaurantDisposable.isDisposed()) {
            oneRestaurantDisposable.dispose();
        }
        oneRestaurantDisposable = streamFetchOneRestaurantResponse(latLng, id, rating)
                .subscribeWith(new DisposableObserver<Restaurant>() {

                    @Override
                    public void onNext(@NonNull Restaurant restaurant) {
                        callback.onSuccess(restaurant);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(e);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }
          // Streams a single restaurant response based on ID and location.
          public Observable<Restaurant> streamFetchOneRestaurantResponse(LatLng latLng, String id, Float rating){
              // Implementation for streaming a single restaurant response.
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
