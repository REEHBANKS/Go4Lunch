package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.os.Build;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.utils.FetchRestaurantsWithSelectedUsersUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FetchRestaurantsWithSelectedUsersUseCaseImpl implements FetchRestaurantsWithSelectedUsersUseCase {

    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    GetCurrentDateUseCase getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    String dateDuJour = getCurrentDateUseCase.execute();


    public FetchRestaurantsWithSelectedUsersUseCaseImpl(
            GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase,
            GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase) {
        this.getAllRestaurantsFromFirebaseUseCase = getAllRestaurantsFromFirebaseUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
    }

    @Override
    public void execute(UseCaseCallback<List<RestaurantWithNumberUser>> callback) {
        TaskCompletionSource<List<Restaurant>> restaurantTaskCompletionSource = new TaskCompletionSource<>();
        TaskCompletionSource<List<SelectedRestaurant>> selectedRestaurantTaskCompletionSource = new TaskCompletionSource<>();

        getAllRestaurantsFromFirebaseUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantTaskCompletionSource.setResult(result);
            }

            @Override
            public void onError(Throwable error) {
                if (error instanceof Exception) {
                    restaurantTaskCompletionSource.setException((Exception) error);
                } else {
                    // Handle or log the error. You can also wrap the Throwable in an Exception if needed.
                    restaurantTaskCompletionSource.setException(new Exception(error));
                }
            }
        });

        getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
            @Override
            public void onSuccess(List<SelectedRestaurant> result) {
                selectedRestaurantTaskCompletionSource.setResult(result);
            }

            @Override
            public void onError(Throwable error) {
                if (error instanceof Exception) {
                    selectedRestaurantTaskCompletionSource.setException((Exception) error);
                } else {
                    // Handle or log the error. You can also wrap the Throwable in an Exception if needed.
                    selectedRestaurantTaskCompletionSource.setException(new Exception(error));
                }
            }
        });

        Task<List<Restaurant>> fetchRestaurantsTask = restaurantTaskCompletionSource.getTask();
        Task<List<SelectedRestaurant>> fetchSelectedRestaurantsTask = selectedRestaurantTaskCompletionSource.getTask();

        Tasks.whenAllSuccess(fetchRestaurantsTask, fetchSelectedRestaurantsTask).addOnSuccessListener(results -> {
            List<Restaurant> restaurants = (List<Restaurant>) results.get(0);
            List<SelectedRestaurant> selectedRestaurants = (List<SelectedRestaurant>) results.get(1);

            // Votre logique pour combiner les deux listes
            HashMap<String, Integer> countMap = new HashMap<>();
            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    countMap.put(selectedRestaurant.getRestaurantId(), countMap.getOrDefault(selectedRestaurant.getRestaurantId(), 0) + 1);
                }
            }

            List<RestaurantWithNumberUser> restaurantWithNumberUsers = new ArrayList<>();
            for (Restaurant restaurant : restaurants) {
                int count = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    count = countMap.getOrDefault(restaurant.getId(), 0);
                }
                restaurantWithNumberUsers.add(new RestaurantWithNumberUser(restaurant, count));
            }

            // Renvoyez le résultat combiné via le callback
            callback.onSuccess(restaurantWithNumberUsers);
        }).addOnFailureListener(e -> {
            callback.onError(e);
        });
    }



}
