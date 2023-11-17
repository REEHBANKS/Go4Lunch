package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedRestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public class GetSelectedRestaurantForCurrentUserUseCase {

    // Use cases for fetching user data, selected restaurants, and all restaurants.
    private final GetCompleteUserDataUseCase getUserDataUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;

    // Constructor initializing with required use cases.
    public GetSelectedRestaurantForCurrentUserUseCase(GetCompleteUserDataUseCase getUserDataUseCase, GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase,
                                                      GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase) {
        this.getUserDataUseCase = getUserDataUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
        this.getAllRestaurantsFromFirebaseUseCase = getAllRestaurantsFromFirebaseUseCase;
    }

    // Executes the use case to find the selected restaurant for the current user.
    public void execute(String dateDuJour, SelectedRestaurantCallback callback) {
        // Fetch user data.
        getUserDataUseCase.execute(new CallbackUserUseCase<User>() {
            @Override
            public void onUserDataFetched(User user) {
                if (user != null) {
                    // Fetch selected restaurants for the given date.
                    getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
                        @Override
                        public void onSuccess(List<SelectedRestaurant> selectedRestaurants) {
                            // Check if the current user has selected any restaurant.
                            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                                if (selectedRestaurant.getUserId().equals(user.getUid())) {
                                    // Fetch all restaurants to find the matching one.
                                    getAllRestaurantsFromFirebaseUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
                                        @Override
                                        public void onSuccess(List<Restaurant> allRestaurants) {
                                            // Find and return the selected restaurant.
                                            // ...
                                        }

                                        @Override
                                        public void onError(Throwable error) {
                                            // Handle errors in fetching all restaurants.
                                            callback.onError(error);
                                        }
                                    });
                                    return;
                                }
                            }
                            // Handle case where no restaurant is found for the user.
                            callback.onNoRestaurantFound();
                        }

                        @Override
                        public void onError(Throwable error) {
                            // Handle errors in fetching selected restaurants.
                            callback.onError(error);
                        }
                    });
                } else {
                    // Handle case where user data is null.
                    callback.onError(new NullPointerException("User is null"));
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle errors in fetching user data.
                callback.onError(e);
            }
        });
    }
}
