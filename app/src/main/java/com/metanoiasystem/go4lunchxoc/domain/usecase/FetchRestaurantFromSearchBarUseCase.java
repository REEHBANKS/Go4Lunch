package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchOneRestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseFetchOneRestaurantCallback;

public class FetchRestaurantFromSearchBarUseCase {

    // Repository for handling restaurant data.
    private final RestaurantRepository restaurantRepository;

    // Constructor to initialize the use case with a restaurant repository.
    public FetchRestaurantFromSearchBarUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Executes the use case to fetch restaurant details based on search parameters.
    public void execute(LatLng latLng, String id, Float rating, UseCaseFetchOneRestaurantCallback<Restaurant> callback) {
        restaurantRepository.fetchOneRestaurantFromNetwork(latLng, id, rating, new RepositoryFetchOneRestaurantCallback<Restaurant>() {

            @Override
            public void onSuccess(Restaurant restaurant) {
                // Callback for successful restaurant retrieval.
                callback.onSuccess(restaurant);
            }

            @Override
            public void onError(Throwable error) {
                // Callback for handling errors during restaurant retrieval.
                callback.onError(error);
            }
        });
    }
}

