package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUserChosenRestaurantsUseCase {

    // Use cases for fetching all restaurants, all users, and all selected restaurants.
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetCurrentDateUseCase  getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    // Get the current date.
    private final String dateDuJour = getCurrentDateUseCase.execute();

    // Constructor initializing with required use cases.
    public GetUserChosenRestaurantsUseCase(GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsUseCase, FetchAllUsersUseCase fetchAllUsersUseCase,
                                           GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase) {
        this.getAllRestaurantsUseCase = getAllRestaurantsUseCase;
        this.fetchAllUsersUseCase = fetchAllUsersUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
    }

    public void execute(UseCaseCallback<List<UserAndPictureWithYourSelectedRestaurant>> callback) {


        // Step 1: Fetch all restaurants.
        getAllRestaurantsUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {

                // Step 2: Fetch all users.
                fetchAllUsersUseCase.execute().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            users.add(document.toObject(User.class));
                        }

                        // Step 3: Fetch all selected restaurants.
                        getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
                            @Override
                            public void onSuccess(List<SelectedRestaurant> selectedRestaurants) {
                                // Combine the data to get the final list.
                                List<UserAndPictureWithYourSelectedRestaurant> result = combineData(restaurants, users, selectedRestaurants);
                                callback.onSuccess(result);
                            }

                            @Override
                            public void onError(Throwable error) {
                                // Handle errors in fetching selected restaurants.
                                callback.onError(error);
                            }
                        });
                    } else {
                        // Handle errors in fetching all users.
                        callback.onError(task.getException());
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                // Handle errors in fetching all restaurants.
                callback.onError(error);
            }
        });
    }

    public List<UserAndPictureWithYourSelectedRestaurant> combineData(List<Restaurant> restaurants, List<User> users, List<SelectedRestaurant> selectedRestaurants) {
        List<UserAndPictureWithYourSelectedRestaurant> combinedList = new ArrayList<>();

        // Create a map for easy lookup of selected restaurants by user ID.
        Map<String, SelectedRestaurant> selectedRestaurantMap = new HashMap<>();
        for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
            selectedRestaurantMap.put(selectedRestaurant.getUserId(), selectedRestaurant);
        }

        // Create a map for easy lookup of restaurants by ID.
        Map<String, Restaurant> restaurantMap = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            restaurantMap.put(restaurant.getId(), restaurant);
        }

        // Combine the data to create a list of UserAndPictureWithYourSelectedRestaurant objects.
        for (User user : users) {
            SelectedRestaurant userSelectedRestaurant = selectedRestaurantMap.get(user.getUid());
            if (userSelectedRestaurant != null) {
                Restaurant correspondingRestaurant = restaurantMap.get(userSelectedRestaurant.getRestaurantId());
                if (correspondingRestaurant != null) {
                    // Add user and their chosen restaurant to the list.
                    combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, correspondingRestaurant));
                } else {
                    // Add user with an empty restaurant if not found in the map.
                    combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, new Restaurant()));
                }
            } else {
                // Add user with an empty restaurant if no selection is found.
                combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, new Restaurant()));
            }
        }

        return combinedList;
    }




}

