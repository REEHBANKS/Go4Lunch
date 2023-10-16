package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebase;

    public RestaurantViewModelFactory(FetchRestaurantListUseCase fetchRestaurantListUseCase,
                                      GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase,
                                      GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebase) {
        this.fetchRestaurantListUseCase = fetchRestaurantListUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
        this.getAllRestaurantsFromFirebase = getAllRestaurantsFromFirebase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d("ViewModelFactory", "Requested ViewModel: " + modelClass.getSimpleName());

        if (modelClass.isAssignableFrom(ListRestaurantsViewModel.class)) {
            return (T) new ListRestaurantsViewModel(fetchRestaurantListUseCase,
                     getAllSelectedRestaurantsUseCase, getAllRestaurantsFromFirebase);
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(fetchRestaurantListUseCase, getAllSelectedRestaurantsUseCase, getAllRestaurantsFromFirebase);


        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
