package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;


import java.util.List;

public class MapViewModel extends ViewModel {

    private final FetchRestaurantListUseCase getAllRestaurantsUseCase;

    public MapViewModel() {
        getAllRestaurantsUseCase = new FetchRestaurantListUseCase();
    }

    public LiveData<List<Restaurant>> getMapLiveData() {
        return getAllRestaurantsUseCase.getRestaurantsLiveData();
    }

    public void fetchMapViewModel(Double latitude, Double longitude) {
        getAllRestaurantsUseCase.execute(latitude, longitude);
    }
}
