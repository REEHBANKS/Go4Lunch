package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.List;

public class GetAllSelectedRestaurantsUseCase {

    // Repository for handling selected restaurant data.
    private final SelectedRestaurantRepository repository;

    // Constructor initializing the use case with a selected restaurant repository.
    public GetAllSelectedRestaurantsUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    // Executes the use case to fetch all selected restaurants for a given date.
    public void execute(String dateDuJour, UseCaseCallback<List<SelectedRestaurant>> callback) {
        repository.getAllSelectedRestaurants(dateDuJour).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<SelectedRestaurant> list = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    list.add(doc.toObject(SelectedRestaurant.class));
                }
                callback.onSuccess(list);
            } else {
                // Handle error if the task is unsuccessful.
                callback.onError(task.getException());
            }
        });
    }
}

