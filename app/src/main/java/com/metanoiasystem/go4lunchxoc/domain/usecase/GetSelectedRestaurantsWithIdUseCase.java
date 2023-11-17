package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;

import java.util.Collections;
import java.util.List;

public class GetSelectedRestaurantsWithIdUseCase {

    // Repository for handling selected restaurant data.
    private final SelectedRestaurantRepository repository;
    // Use case for obtaining the current date.
    private final GetCurrentDateUseCase getCurrentDateUseCase;

    // Constructor initializing with the selected restaurant repository and the current date use case.
    public GetSelectedRestaurantsWithIdUseCase(SelectedRestaurantRepository repository, GetCurrentDateUseCase getCurrentDateUseCase) {
        this.repository = repository;
        this.getCurrentDateUseCase = getCurrentDateUseCase;
    }

    // Executes the use case to fetch selected restaurants by a specific ID for the current date.
    public Task<List<SelectedRestaurant>> execute(String restaurantId) {
        return repository.getAllSelectedRestaurantsWithId(restaurantId, getCurrentDateUseCase.execute())
                .continueWith(task -> {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        // Convert the query snapshot to a list of SelectedRestaurant objects.
                        return querySnapshot.toObjects(SelectedRestaurant.class);
                    } else {
                        // Return an empty list if no results are found.
                        return Collections.emptyList();
                    }
                });
    }
}

