package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchAllRestaurantFetchCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public class FetchRestaurantListUseCase {

    // Repository for handling restaurant data.
    private final RestaurantRepository restaurantRepository;

    // Constructor to initialize the use case with a restaurant repository.
    public FetchRestaurantListUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Executes the use case to fetch a list of restaurants based on latitude and longitude.
    public void execute(double latitude, double longitude, UseCaseCallback<List<Restaurant>> callback) {
        Log.d("RestaurantRepo", "Fetching restaurants for lat: " + latitude + " long: " + longitude);
        restaurantRepository.fetchRestaurant(latitude, longitude, new RepositoryFetchAllRestaurantFetchCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                // Callback for successful retrieval of the restaurant list.
                callback.onSuccess(restaurants);
            }

            @Override
            public void onError(Throwable error) {
                // Callback for handling errors during the retrieval of the restaurant list.
                callback.onError(error);
            }
        });
    }
}
