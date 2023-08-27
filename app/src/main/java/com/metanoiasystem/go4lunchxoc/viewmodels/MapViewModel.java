package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecases.GetAllRestaurantsUseCase;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final GetAllRestaurantsUseCase getRestaurantsUseCase;

    public MapViewModel() {
        getRestaurantsUseCase = new GetAllRestaurantsUseCase();
    }

    public LiveData<List<Restaurant>> getMapLiveData() {
        return getRestaurantsUseCase.getRestaurantsLiveData();
    }

    public void fetchMapViewModel(Double latitude, Double longitude) {
        getRestaurantsUseCase.execute(latitude, longitude);
    }
}
