package com.metanoiasystem.go4lunchxoc.domain.usecases;

import androidx.lifecycle.LiveData;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;

import java.util.List;

public class GetAllRestaurantsUseCase {

    private final RestaurantRepository restaurantRepository;

    public GetAllRestaurantsUseCase() {
        this.restaurantRepository = RestaurantRepository.getInstance();
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantRepository.getRestaurantLiveData();
    }

    public void execute(Double latitude, Double longitude) {
        restaurantRepository.fetchRestaurant(latitude, longitude);
    }
}
