package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchAllRestaurantFetchCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public class FetchRestaurantListUseCase {

    private final RestaurantRepository restaurantRepository;

    public FetchRestaurantListUseCase() {
        this.restaurantRepository = RestaurantRepository.getInstance();
    }

    public void execute(double latitude, double longitude, UseCaseCallback<List<Restaurant>> callback) {
        Log.d("RestaurantRepo", "Fetching restaurants for lat: " + latitude + " long: " + longitude);
        restaurantRepository.fetchRestaurant(latitude, longitude, new RepositoryFetchAllRestaurantFetchCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                callback.onSuccess(restaurants);

            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);

            }


        });
    }



}
