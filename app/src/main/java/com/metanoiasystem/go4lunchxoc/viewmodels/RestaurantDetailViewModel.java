package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCurrentUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public class RestaurantDetailViewModel extends ViewModel {

    // Use cases for adding to favorites, creating new selections, and checking and handling existing selections
    private final AddToFavoritesUseCase createAddRestaurantFavoritesUseCase;
    private final CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase;
    private final GetCurrentUseCase getCurrentUseCase;
    private final GetCurrentDateUseCase getCurrentDateUseCase;

    public RestaurantDetailViewModel() {
        // Initializing the use cases with Injector provided instances
        createAddRestaurantFavoritesUseCase = Injector.provideAddToFavoritesUseCase();

        checkAndHandleExistingRestaurantSelectionUseCase = Injector.provideCheckAndHandleExistingRestaurantSelectionUseCase();
        getCurrentDateUseCase = Injector.provideGetCurrentDateUseCase();
        getCurrentUseCase = Injector.provideGetCurrentUseCase();
    }

    // LiveData for indicating successful addition to favorites
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    // Accessor for addSuccess LiveData
    public LiveData<Boolean> getAddSuccess() {
        return addSuccessLiveData;
    }

    // LiveData for holding any error messages
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;




    // LiveData for tracking if a restaurant selection was updated or created
    private final MutableLiveData<Boolean> restaurantUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> restaurantCreated = new MutableLiveData<>();

    // Accessors for restaurantUpdated and restaurantCreated LiveData
    public LiveData<Boolean> isRestaurantUpdated() {
        return restaurantUpdated;
    }
    public LiveData<Boolean> isRestaurantCreated() {
        return restaurantCreated;
    }

    // Method to add a new restaurant to favorites
    public void createNewRestaurantFavorites(String restaurantId) {
        createAddRestaurantFavoritesUseCase.execute(restaurantId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addSuccessLiveData.setValue(true);
                Log.d("FavoriteRestaurantVM", "Successfully added restaurant to favorites.");
            } else {
                _errorMessage.setValue("Error adding restaurant to favorites.");
                Log.e("FavoriteRestaurantVM", "Error adding restaurant to favorites.", task.getException());
            }
        });
    }

    // Method to create or update a selected restaurant
    public void createOrUpdateSelectedRestaurant(String restaurantId) {
        FirebaseUser user = getCurrentUseCase.execute();
        if (user != null && restaurantId != null) {
            String userId = user.getUid();
            String dateDeJour = getCurrentDateUseCase.execute();
            checkAndHandleExistingRestaurantSelectionUseCase.execute(restaurantId, userId, dateDeJour, new SelectedUseCaseCallback() {
                @Override
                public void onSuccess() {
                    restaurantCreated.setValue(true);
                }
                @Override
                public void onError(Exception e) {
                    restaurantCreated.setValue(false);
                }
            });
        }
    }
}







