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

    private final SelectedRestaurantRepository repository;

    public GetAllSelectedRestaurantsUseCase() {
        repository = Injector.provideSelectedRestaurantRepository();
    }

    public void execute(String dateDuJour,UseCaseCallback<List<SelectedRestaurant>> callback) {
        repository.getAllSelectedRestaurants( dateDuJour).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<SelectedRestaurant> list = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    list.add(doc.toObject(SelectedRestaurant.class));
                }
                Log.d("ObserverDebug", "usecaseLiveData changed");
                callback.onSuccess(list);

            } else {
                callback.onError(task.getException());
            }
        });
    }
}
