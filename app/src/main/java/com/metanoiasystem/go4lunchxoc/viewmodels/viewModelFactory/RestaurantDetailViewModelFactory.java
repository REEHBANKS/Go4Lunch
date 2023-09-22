package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantDetailViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.UserViewModel;

public class RestaurantDetailViewModelFactory implements ViewModelProvider.Factory {

    private final AddToFavoritesUseCase addToFavoritesUseCase;


    public RestaurantDetailViewModelFactory(AddToFavoritesUseCase addToFavoritesUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            @SuppressWarnings("unchecked")
            T viewModel = (T) new RestaurantDetailViewModel(addToFavoritesUseCase);
            return viewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
