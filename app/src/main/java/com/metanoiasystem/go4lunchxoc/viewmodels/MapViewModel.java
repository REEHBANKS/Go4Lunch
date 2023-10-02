package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;


import java.util.List;

public class MapViewModel extends ViewModel {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();

    public MapViewModel() {
        fetchRestaurantListUseCase = new FetchRestaurantListUseCase();
    }

    public LiveData<List<Restaurant>> getMapLiveData() {
        return restaurantsLiveData;
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }


    public void fetchRestaurants(Double latitude, Double longitude) {
        fetchRestaurantListUseCase.execute(latitude, longitude, new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantsLiveData.setValue(result);
            }

            @Override
            public void onError(Throwable error) {
                errorLiveData.setValue(error);
            }
        });
    }
}
