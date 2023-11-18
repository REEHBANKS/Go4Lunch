package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantSelectorListViewModel extends ViewModel {

    private final GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;

    public RestaurantSelectorListViewModel() {
        getSelectedRestaurantsWithIdUseCase = Injector.provideGetSelectedRestaurantsWithIdUseCase();
        fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();
    }

    // LiveData indicates changes in the list
    private final MutableLiveData<List<User>> usersLiveDataDetailScreen = new MutableLiveData<>();

    public LiveData<List<User>> getUsersDetailScreen() {
        return usersLiveDataDetailScreen;
    }

    public void getUsersWhoSelectedThisRestaurant(String restaurantId){

        // Success listener for fetching selected restaurants by their IDs
        getSelectedRestaurantsWithIdUseCase.execute(restaurantId).addOnSuccessListener(selectedRestaurants -> {
            // Create a map to quickly check if a restaurant is selected by any user
            HashMap<String, SelectedRestaurant> selectedRestaurantMap = new HashMap<>();
            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                selectedRestaurantMap.put(selectedRestaurant.getUserId(), selectedRestaurant);
            }

            // Fetch all users and add them to a list
            fetchAllUsersUseCase.execute().addOnSuccessListener(querySnapshot -> {
                List<User> allUsers = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        allUsers.add(user);
                    }
                }

                // Filter users who have selected the restaurant
                List<User> selectedUsers = new ArrayList<>();
                for (User user : allUsers) {
                    if (selectedRestaurantMap.containsKey(user.getUid())) {
                        selectedUsers.add(user);
                    }
                }

                // Update the LiveData with the list of selected users
                usersLiveDataDetailScreen.setValue(selectedUsers);
            });
        });
    }
}




