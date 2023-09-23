package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;

public class CreateNewSelectedRestaurantUseCase {

    private final SelectedRestaurantRepository repository;

    public CreateNewSelectedRestaurantUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    public Task<DocumentReference> execute(String restaurantId, String userId, String date) {
        return repository.createNewSelectedRestaurant(restaurantId, userId, date);
    }
}
