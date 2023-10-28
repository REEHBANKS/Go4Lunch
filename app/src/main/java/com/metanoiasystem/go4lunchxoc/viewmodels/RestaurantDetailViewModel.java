package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.utils.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantDetailViewModel extends ViewModel {

    private final AddToFavoritesUseCase createAddRestaurantFavoritesUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;
    private final CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase;
    private final GetCurrentUseCase getCurrentUseCase;
    private final GetCurrentDateUseCase getCurrentDateUseCase;




    public RestaurantDetailViewModel(){
        createAddRestaurantFavoritesUseCase= Injector.provideAddToFavoritesUseCase();
        createNewSelectedRestaurantUseCase = Injector.provideCreateNewSelectedRestaurantUseCase();
        checkAndHandleExistingRestaurantSelectionUseCase = Injector.provideCheckAndHandleExistingRestaurantSelectionUseCase();
        getCurrentDateUseCase = Injector.provideGetCurrentDateUseCase();
        getCurrentUseCase =Injector.provideGetCurrentUseCase();

    }

    // LiveData to indicate a successful addition to favorites
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getAddSuccess() {
        return addSuccessLiveData;
    }

    // LiveData to hold any error message
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    // LiveData to indicate a successful addition to selected
    private final MutableLiveData<Boolean> restaurantSelected = new MutableLiveData<>();
    public LiveData<Boolean> getRestaurantSelected() {
        return restaurantSelected;
    }

    //TODO LIVEDATA TEST

    private final MutableLiveData<Boolean> restaurantUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> restaurantCreated = new MutableLiveData<>();

    public LiveData<Boolean> isRestaurantUpdated() {
        return restaurantUpdated;
    }

    public LiveData<Boolean> isRestaurantCreated() {
        return restaurantCreated;
    }



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






