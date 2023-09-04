package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;

import java.util.List;

public class ListRestaurantsViewModel extends ViewModel {

    private final FetchRestaurantListUseCase getAllRestaurantsUseCase;

    public ListRestaurantsViewModel() {
        getAllRestaurantsUseCase = new FetchRestaurantListUseCase();
    }

    public LiveData<List<Restaurant>> getListRestaurants() {
        return getAllRestaurantsUseCase.getRestaurantsLiveData();
    }

    public void fetchListRestaurants(Double latitude, Double longitude) {
        getAllRestaurantsUseCase.execute(latitude, longitude);
    }
}
