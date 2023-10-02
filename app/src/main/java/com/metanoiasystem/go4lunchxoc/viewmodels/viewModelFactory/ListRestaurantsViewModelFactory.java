package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CountUsersForRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;

public class ListRestaurantsViewModelFactory implements ViewModelProvider.Factory {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final CountUsersForRestaurantUseCase countUsersForRestaurantUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;

    public ListRestaurantsViewModelFactory(FetchRestaurantListUseCase fetchRestaurantListUseCase,
                                           CountUsersForRestaurantUseCase countUsersForRestaurantUseCase,
                                           GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase) {
        this.fetchRestaurantListUseCase = fetchRestaurantListUseCase;
        this.countUsersForRestaurantUseCase = countUsersForRestaurantUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListRestaurantsViewModel.class)) {
            return (T) new ListRestaurantsViewModel(fetchRestaurantListUseCase,
                    countUsersForRestaurantUseCase, getAllSelectedRestaurantsUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
