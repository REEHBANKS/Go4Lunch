package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;

public class AddToFavoritesUseCase {
    // Reference to the FavoriteRestaurantRepository.
    private final FavoriteRestaurantRepository repository;

    // Constructor to initialize the use case with the repository.
    public AddToFavoritesUseCase(FavoriteRestaurantRepository repository) {
        this.repository = repository;
    }

    // Executes the operation to add a restaurant to favorites.
    // Returns a Task<Void> indicating the completion status of the operation.
    public Task<Void> execute(String restaurantId) {
        return repository.createFavoriteRestaurant(restaurantId);
    }
}

