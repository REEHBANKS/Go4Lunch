package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;

public class AddToFavoritesUseCase {
    private final FavoriteRestaurantRepository repository;

    public AddToFavoritesUseCase(FavoriteRestaurantRepository repository) {
        this.repository = repository;
    }

    public Task<Void> execute(String restaurantId) {
        return repository.createFavoriteRestaurant(restaurantId);
    }



}
