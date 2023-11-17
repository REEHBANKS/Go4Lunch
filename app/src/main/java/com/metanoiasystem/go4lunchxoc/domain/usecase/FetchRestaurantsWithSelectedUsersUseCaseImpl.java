package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.os.Build;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.FetchRestaurantsWithSelectedUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class FetchRestaurantsWithSelectedUsersUseCaseImpl implements FetchRestaurantsWithSelectedUsersUseCase {

    // Dependencies for fetching all restaurants and all selected restaurants.
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    GetCurrentDateUseCase getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    // Get current date to filter the selected restaurants.
    String dateDuJour = getCurrentDateUseCase.execute();

    // Constructor initializing the use case with necessary dependencies.
    public FetchRestaurantsWithSelectedUsersUseCaseImpl(
            GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase,
            GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase) {
        this.getAllRestaurantsFromFirebaseUseCase = getAllRestaurantsFromFirebaseUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
    }

    @Override
    public void execute(UseCaseCallback<List<RestaurantWithNumberUser>> callback) {
        // Task completion sources for fetching restaurants and selected restaurants.
        TaskCompletionSource<List<Restaurant>> restaurantTaskCompletionSource = new TaskCompletionSource<>();
        TaskCompletionSource<List<SelectedRestaurant>> selectedRestaurantTaskCompletionSource = new TaskCompletionSource<>();

        // Execute use case to fetch all restaurants.
        getAllRestaurantsFromFirebaseUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantTaskCompletionSource.setResult(result);
            }

            @Override
            public void onError(Throwable error) {
                // Handle errors for fetching restaurants.
                restaurantTaskCompletionSource.setException(error instanceof Exception ? (Exception) error : new Exception(error));
            }
        });

        // Execute use case to fetch selected restaurants for the current date.
        getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
            @Override
            public void onSuccess(List<SelectedRestaurant> result) {
                selectedRestaurantTaskCompletionSource.setResult(result);
            }

            @Override
            public void onError(Throwable error) {
                // Handle errors for fetching selected restaurants.
                selectedRestaurantTaskCompletionSource.setException(error instanceof Exception ? (Exception) error : new Exception(error));
            }
        });

        Task<List<Restaurant>> fetchRestaurantsTask = restaurantTaskCompletionSource.getTask();
        Task<List<SelectedRestaurant>> fetchSelectedRestaurantsTask = selectedRestaurantTaskCompletionSource.getTask();

        // Combine the tasks and process the results once both are successful.
        Tasks.whenAllSuccess(fetchRestaurantsTask, fetchSelectedRestaurantsTask).addOnSuccessListener(results -> {
            List<Restaurant> restaurants = (List<Restaurant>) results.get(0);
            List<SelectedRestaurant> selectedRestaurants = (List<SelectedRestaurant>) results.get(1);

            // Logic to combine the two lists:
            // Create a map to count the number of selections for each restaurant.
            HashMap<String, Integer> countMap = new HashMap<>();
            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    countMap.put(selectedRestaurant.getRestaurantId(), countMap.getOrDefault(selectedRestaurant.getRestaurantId(), 0) + 1);
                }
            }

            // Combine restaurant details with the selection count.
            List<RestaurantWithNumberUser> restaurantWithNumberUsers = new ArrayList<>();
            for (Restaurant restaurant : restaurants) {
                int count = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    count = countMap.getOrDefault(restaurant.getId(), 0);
                }
                restaurantWithNumberUsers.add(new RestaurantWithNumberUser(restaurant, count));
            }

            // Return the combined result via the callback.
            callback.onSuccess(restaurantWithNumberUsers);
        }).addOnFailureListener(e -> {
            callback.onError(e);
        });
    }



}
