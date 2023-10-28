package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUserChosenRestaurantsUseCase {

    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    List<SelectedRestaurant> listAllSelectedRestaurants = new ArrayList<>();
    List<User> listAllUsers = new ArrayList<>();
    List<Restaurant> restaurantList = new ArrayList<>();
    GetCurrentDateUseCase getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    String dateDuJour = getCurrentDateUseCase.execute();

    public GetUserChosenRestaurantsUseCase() {
        this.getAllRestaurantsUseCase = Injector.provideGetAllRestaurantsFromFirebaseUseCase();
        this.fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();
        this.getAllSelectedRestaurantsUseCase = Injector.provideGetAllSelectedRestaurantsUseCase();
    }

    public void execute(UseCaseCallback<List<UserAndPictureWithYourSelectedRestaurant>> callback) {
        Log.d("GetUserChosen", "Début de la méthode execute");

        // 1. Fetch all restaurants
        getAllRestaurantsUseCase.execute(new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                Log.d("GetUserChosen", "Restaurants récupérés : " + restaurants.size());
                restaurantList = restaurants;

                // 2. Fetch all users
                fetchAllUsersUseCase.execute().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            users.add(document.toObject(User.class));
                        }
                        Log.d("GetUserChosen", "Utilisateurs récupérés : " + users.size());
                        listAllUsers = users;

                        // 3. Fetch all selected restaurants
                        getAllSelectedRestaurantsUseCase.execute(dateDuJour, new UseCaseCallback<List<SelectedRestaurant>>() {
                            @Override
                            public void onSuccess(List<SelectedRestaurant> selectedRestaurants) {
                                Log.d("GetUserChosen", "Restaurants sélectionnés récupérés : " + selectedRestaurants.size());

                                // Combine the data to get the final list
                                List<UserAndPictureWithYourSelectedRestaurant> result = combineData(restaurants, users, selectedRestaurants);
                                Log.d("GetUserChosen", "Liste finale combinée : " + result.size());
                                callback.onSuccess(result);
                            }

                            @Override
                            public void onError(Throwable error) {
                                Log.e("GetUserChosen", "Erreur lors de la récupération des restaurants sélectionnés", error);
                                callback.onError(error);
                            }
                        });
                    } else {
                        Log.e("GetUserChosen", "Erreur lors de la récupération des utilisateurs", task.getException());
                        callback.onError(task.getException());
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                Log.e("GetUserChosen", "Erreur lors de la récupération des restaurants", error);
                callback.onError(error);
            }
        });
    }


    public List<UserAndPictureWithYourSelectedRestaurant> combineData(List<Restaurant> restaurants, List<User> users, List<SelectedRestaurant> selectedRestaurants) {
        List<UserAndPictureWithYourSelectedRestaurant> combinedList = new ArrayList<>();

        // Création d'une map pour faciliter la recherche des restaurants sélectionnés par ID
        Map<String, SelectedRestaurant> selectedRestaurantMap = new HashMap<>();
        for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
            selectedRestaurantMap.put(selectedRestaurant.getUserId(), selectedRestaurant);
        }

        // Création d'une map pour faciliter la recherche des restaurants par ID
        Map<String, Restaurant> restaurantMap = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            restaurantMap.put(restaurant.getId(), restaurant);
        }

        // Combinaison des données
        for (User user : users) {
            SelectedRestaurant userSelectedRestaurant = selectedRestaurantMap.get(user.getUid());
            if (userSelectedRestaurant != null) {
                Restaurant correspondingRestaurant = restaurantMap.get(userSelectedRestaurant.getRestaurantId());
                if (correspondingRestaurant != null) {
                    combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, correspondingRestaurant));
                } else {
                    combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, new Restaurant()));
                }
            } else {
                combinedList.add(new UserAndPictureWithYourSelectedRestaurant(user, new Restaurant()));
            }
        }

        return combinedList;
    }



}

