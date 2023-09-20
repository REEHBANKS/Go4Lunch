package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;

import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {

    private final AddToFavoritesUseCase addToFavoritesUseCase;

    public RestaurantDetailViewModel(AddToFavoritesUseCase addToFavoritesUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
    }

    // LiveData to indicate a successful addition to favorites
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    // LiveData to hold any error message
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    public LiveData<Boolean> getAddSuccess() {
        return addSuccessLiveData;
    }

    public void addRestaurantToFavorites(String restaurantId) {
        addToFavoritesUseCase.execute(restaurantId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addSuccessLiveData.setValue(true);
                Log.d("FavoriteRestaurantVM", "Successfully added restaurant to favorites.");
            } else {
                _errorMessage.setValue("Error adding restaurant to favorites.");
                Log.e("FavoriteRestaurantVM", "Error adding restaurant to favorites.", task.getException());
            }
        });
    }


}
