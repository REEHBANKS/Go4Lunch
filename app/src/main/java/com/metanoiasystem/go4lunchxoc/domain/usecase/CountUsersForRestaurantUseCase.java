package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public class CountUsersForRestaurantUseCase {

    private final SelectedRestaurantRepository repository;


    public CountUsersForRestaurantUseCase(SelectedRestaurantRepository repository) {
        this.repository = repository;
    }

    public void execute(Restaurant restaurant, List<SelectedRestaurant> allSelectedRestaurants, UseCaseCallback<Integer> callback) {
        if (allSelectedRestaurants == null) {
            callback.onSuccess(0);
            return;
        }

        int count = 0;
        for (SelectedRestaurant selectedRestaurant : allSelectedRestaurants) {
            if (restaurant.getId().equals(selectedRestaurant.getRestaurantId())) {
                count++;
            }
        }
        callback.onSuccess(count);
    }

}
