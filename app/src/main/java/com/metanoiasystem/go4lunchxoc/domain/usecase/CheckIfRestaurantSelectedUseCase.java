package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;

public class CheckIfRestaurantSelectedUseCase {

    // Reference to the SelectedRestaurantRepository.
    private final SelectedRestaurantRepository repository;

    // Constructor initializing the use case with a repository instance.
    public CheckIfRestaurantSelectedUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    // Executes the operation to check if a restaurant is selected by a user on a given date.
    // Returns a Task<QuerySnapshot> which can be used to determine the result of the query.
    public Task<QuerySnapshot> execute(String userId, String date) {
        return repository.checkIfRestaurantSelected(userId, date);
    }
}

