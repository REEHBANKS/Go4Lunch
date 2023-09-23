package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateSelectedRestaurantUseCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {

    private final AddToFavoritesUseCase addToFavoritesUseCase;
    private final CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;
    private final UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase;



    public RestaurantDetailViewModel(AddToFavoritesUseCase addToFavoritesUseCase,
                                     CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase,
                                     CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase,
                                     UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
        this.checkIfRestaurantSelectedUseCase = checkIfRestaurantSelectedUseCase;
        this.createNewSelectedRestaurantUseCase = createNewSelectedRestaurantUseCase;
        this.updateSelectedRestaurantUseCase = updateSelectedRestaurantUseCase;
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

    public void createOrUpdateSelectedRestaurant(String restaurantId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && restaurantId != null) {
            String userId = user.getUid();
            String dateDeJour = new SimpleDateFormat("dd/MM/yy").format(new Date());

            checkIfRestaurantSelectedUseCase.execute(userId, dateDeJour)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Update existing selected restaurant
                                DocumentSnapshot existingSelectedRestaurant = querySnapshot.getDocuments().get(0);
                                updateSelectedRestaurantUseCase.execute(existingSelectedRestaurant.getReference(), restaurantId, dateDeJour)
                                        .addOnCompleteListener(updateTask -> restaurantSelected.setValue(updateTask.isSuccessful()));
                            } else {
                                // Create new selected restaurant
                                createNewSelectedRestaurantUseCase.execute(restaurantId, userId, dateDeJour)
                                        .addOnCompleteListener(createTask -> restaurantSelected.setValue(createTask.isSuccessful()));
                            }
                        }
                    });
        }
    }

}






