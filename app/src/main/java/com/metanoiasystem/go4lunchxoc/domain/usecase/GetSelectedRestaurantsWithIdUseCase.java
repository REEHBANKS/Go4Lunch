package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;

import java.util.Collections;
import java.util.List;

public class GetSelectedRestaurantsWithIdUseCase {

    private final SelectedRestaurantRepository repository;
    private final GetCurrentDateUseCase getCurrentDateUseCase;

    public GetSelectedRestaurantsWithIdUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
        getCurrentDateUseCase = Injector.provideGetCurrentDateUseCase();
    }

    public Task<List<SelectedRestaurant>> execute(String restaurantId) {
        return repository.getAllSelectedRestaurantsWithId(restaurantId,getCurrentDateUseCase.execute())
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
