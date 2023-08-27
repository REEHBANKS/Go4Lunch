package com.metanoiasystem.go4lunchxoc.data.repository;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    // MutableLiveData that will hold a list of all restaurants.
            MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

    // Function to get the LiveData object that holds the list of all restaurants.
    public LiveData<List<Restaurant>> getRestaurantLiveData() {
        return result;
    }

    // MutableLiveData that will hold data of a single restaurant
    MutableLiveData<Restaurant> oneResult = new MutableLiveData<>();

    // Function to get the LiveData object that holds data of a single restaurant.
    public LiveData<Restaurant> getOneRestaurantLiveData() {
        return oneResult;
    }

    // Disposables are used in RxJava to manage memory. When you don't need your Observable anymore you can dispose it.
    // In this case, we have a Disposable to manage the streamFetchOneRestaurantResponse Observable.
    private Disposable restaurantDisposable;
    private Disposable oneRestaurantDisposable;

    // Singleton instance of the RestaurantRepository class.
    private static RestaurantRepository restaurantRepository;

    // String constant to represent all restaurants field.
    private static final String ALL_RESTAURANTS_FIELD = "all_restaurants";

    // Singleton getter function for the RestaurantRepository class.
    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;

    }


    // Function to fetch all restaurants. It calls the function to fetch all restaurants and then updates
    // the MutableLiveData object with the fetched data.
    public void fetchRestaurant() {
        // If a previous fetch operation was still ongoing, it is stopped.
        if (restaurantDisposable != null && !restaurantDisposable.isDisposed()) {
            restaurantDisposable.dispose();
        }

        // Start a new fetch operation.
        restaurantDisposable = streamFetchRestaurantResponse()
                .subscribeWith(new DisposableObserver<List<Restaurant>>() {

                    @Override
                    public void onNext(@NonNull List<Restaurant> restaurants) {
                        result.setValue(restaurants);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("Renel", "On error");

                    }

                    @Override
                    public void onComplete() {
                        Log.d("Renel", "On complete");

                    }
                });
    }

    // Function that makes a request to the RestaurantService to fetch all restaurants.
    // It then maps the response to a list of Restaurant objects.
    public Observable<List<Restaurant>> streamFetchRestaurantResponse() {

      //  String lat = ,
      //  String lng =
        String location = "49.1479317" + "," + "2.2466113";
        double latitude = 49.1479317;
        double longitude = 2.2466113;


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

                    // Log de la liste des restaurants avant de la retourner
                    for (Restaurant restaurant : restaurants) {
                        Log.d("RestaurantList", restaurant.toString());
                    }

                    return restaurants;
                })


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
