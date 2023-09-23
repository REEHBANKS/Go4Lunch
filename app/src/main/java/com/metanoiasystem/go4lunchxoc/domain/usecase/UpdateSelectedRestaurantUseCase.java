package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;

public class UpdateSelectedRestaurantUseCase {

    private final SelectedRestaurantRepository repository;

    public UpdateSelectedRestaurantUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    public Task<Void> execute(DocumentReference restaurantRef, String restaurantId, String date) {
        return repository.updateSelectedRestaurant(restaurantRef, restaurantId, date);
    }
}
