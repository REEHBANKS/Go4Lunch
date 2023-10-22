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

    private final GetCompleteUserDataUseCase getUserDataUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;

    public GetSelectedRestaurantForCurrentUserUseCase() {
        getUserDataUseCase = Injector.provideGetCompleteUserDataUseCase();
        getAllSelectedRestaurantsUseCase = Injector.provideGetAllSelectedRestaurantsUseCase();
        getAllRestaurantsFromFirebaseUseCase = Injector.provideGetAllRestaurantsFromFirebaseUseCase();
    }

    public void execute(String dateDuJour, SelectedRestaurantCallback callback) {

        getUserDataUseCase.execute(new CallbackUserUseCase<User>() {
            @Override
            public void onUserDataFetched(User user) {
                if (user != null) {
                    getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
                        @Override
                        public void onSuccess(List<SelectedRestaurant> selectedRestaurants) {
                            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                                if (selectedRestaurant.getUserId().equals(user.getUid())) {
                                    // Fetch all restaurants to find the matching one
                                    getAllRestaurantsFromFirebaseUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
                                        @Override
                                        public void onSuccess(List<Restaurant> allRestaurants) {
                                            for (Restaurant restaurant : allRestaurants) {
                                                if (restaurant.getId().equals(selectedRestaurant.getRestaurantId())) {
                                                    callback.onRestaurantFound(restaurant);
                                                    return;
                                                }
                                            }
                                            callback.onNoRestaurantFound();
                                        }

                                        @Override
                                        public void onError(Throwable error) {
                                            callback.onError(error);
                                        }
                                    });
                                    return;
                                }
                            }
                            callback.onNoRestaurantFound();
                        }

                        @Override
                        public void onError(Throwable error) {
                            callback.onError(error);
                        }
                    });
                } else {
                    callback.onError(new NullPointerException("User is null"));
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}



