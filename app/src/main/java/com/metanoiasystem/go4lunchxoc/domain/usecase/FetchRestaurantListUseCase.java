package com.metanoiasystem.go4lunchxoc.domain.usecase;

import androidx.lifecycle.LiveData;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;

import java.util.List;

public class FetchRestaurantListUseCase {

    private final RestaurantRepository restaurantRepository;

    public FetchRestaurantListUseCase() {
        this.restaurantRepository = RestaurantRepository.getInstance();
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantRepository.getRestaurantLiveData();
    }

    public void execute(Double latitude, Double longitude) {
        restaurantRepository.fetchRestaurant(latitude, longitude);
    }

}
