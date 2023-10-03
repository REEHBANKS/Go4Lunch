package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CountUsersForRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final CountUsersForRestaurantUseCase countUsersForRestaurantUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;

    public RestaurantViewModelFactory(FetchRestaurantListUseCase fetchRestaurantListUseCase,
                                      CountUsersForRestaurantUseCase countUsersForRestaurantUseCase,
                                      GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase) {
        this.fetchRestaurantListUseCase = fetchRestaurantListUseCase;
        this.countUsersForRestaurantUseCase = countUsersForRestaurantUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d("ViewModelFactory", "Requested ViewModel: " + modelClass.getSimpleName());

        if (modelClass.isAssignableFrom(ListRestaurantsViewModel.class)) {
            return (T) new ListRestaurantsViewModel(fetchRestaurantListUseCase,
                    countUsersForRestaurantUseCase, getAllSelectedRestaurantsUseCase);
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(fetchRestaurantListUseCase);


        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
