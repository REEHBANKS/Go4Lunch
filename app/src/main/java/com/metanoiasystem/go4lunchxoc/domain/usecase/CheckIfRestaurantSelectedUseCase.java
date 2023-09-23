package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;

public class CheckIfRestaurantSelectedUseCase {

    private final SelectedRestaurantRepository repository;

    public CheckIfRestaurantSelectedUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    public Task<QuerySnapshot> execute(String userId, String date) {
        return repository.checkIfRestaurantSelected(userId, date);
    }
}
