package com.metanoiasystem.go4lunchxoc.domain.usecase;

import androidx.lifecycle.LiveData;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public class FetchRestaurantListUseCase {

    private final RestaurantRepository restaurantRepository;

    public FetchRestaurantListUseCase() {
        this.restaurantRepository = RestaurantRepository.getInstance();
    }

    public void execute(Double latitude, Double longitude, UseCaseCallback<List<Restaurant>> callback) {
        restaurantRepository.fetchRestaurant(latitude, longitude, new RestaurantRepository.RestaurantFetchCallback() {
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
