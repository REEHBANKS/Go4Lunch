package com.metanoiasystem.go4lunchxoc.domain.usecase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.List;

public class GetAllRestaurantsFromFirebaseUseCase {

    // Repository for handling restaurant data.
    private final RestaurantRepository restaurantRepository;

    // Constructor initializing the use case with a restaurant repository.
    public GetAllRestaurantsFromFirebaseUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Executes the use case to fetch all restaurants from Firebase.
    public void execute(UseCaseCallback<List<Restaurant>> callback) {
        restaurantRepository.getAllRestaurantsFromFirebase().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Restaurant> restaurantList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurantList.add(restaurant);
                    }
                    callback.onSuccess(restaurantList);
                } else {
                    // Handle error if the task is unsuccessful.
                    callback.onError(task.getException());
                }
            }
        });
    }
}

