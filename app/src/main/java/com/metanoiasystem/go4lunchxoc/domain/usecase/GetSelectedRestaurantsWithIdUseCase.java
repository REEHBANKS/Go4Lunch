package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;

import java.util.Collections;
import java.util.List;

public class GetSelectedRestaurantsWithIdUseCase {

    private final SelectedRestaurantRepository repository;

    public GetSelectedRestaurantsWithIdUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    public Task<List<SelectedRestaurant>> execute(String restaurantId) {
        return repository.getAllSelectedRestaurantsWithId(restaurantId)
                .continueWith(task -> {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        return querySnapshot.toObjects(SelectedRestaurant.class);
                    } else {
                        return Collections.emptyList();
                    }
                });
    }
}
