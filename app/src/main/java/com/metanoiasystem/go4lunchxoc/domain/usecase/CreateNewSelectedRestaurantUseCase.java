package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public class CreateNewSelectedRestaurantUseCase {

    private final SelectedRestaurantRepository repository;

    public CreateNewSelectedRestaurantUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }



    public void execute(String restaurantId, String userId, String date, SelectedUseCaseCallback callback) {
        repository.createNewSelectedRestaurant(restaurantId, userId, date)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

}
