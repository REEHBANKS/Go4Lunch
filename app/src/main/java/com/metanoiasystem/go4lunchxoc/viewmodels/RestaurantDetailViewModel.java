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
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateSelectedRestaurantUseCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantDetailViewModel extends ViewModel {

    private final AddToFavoritesUseCase addToFavoritesUseCase;
    private final CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;
    private final UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase;



    public RestaurantDetailViewModel(AddToFavoritesUseCase addToFavoritesUseCase,
                                     CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase,
                                     CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase,
                                     UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase, GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase) {
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

    //TODO LIVEDATA TEST

    private final MutableLiveData<Boolean> restaurantUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> restaurantCreated = new MutableLiveData<>();

    public LiveData<Boolean> isRestaurantUpdated() {
        return restaurantUpdated;
    }

    public LiveData<Boolean> isRestaurantCreated() {
        return restaurantCreated;
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
        Log.d("MyApp", "Méthode createOrUpdateSelectedRestaurant appelée avec restaurantId: " + restaurantId);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && restaurantId != null) {
            String userId = user.getUid();
            Log.d("MyApp", "Utilisateur actuel obtenu: " + user.getUid());
            String dateDeJour = new SimpleDateFormat("dd/MM/yy").format(new Date());

            checkIfRestaurantSelectedUseCase.execute(userId, dateDeJour)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            Log.d("MyApp", "checkIfRestaurantSelectedUseCase réussi. Nombre de restaurants trouvés: " + (querySnapshot != null ? querySnapshot.size() : 0));

                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Update existing selected restaurant
                                DocumentSnapshot existingSelectedRestaurant = querySnapshot.getDocuments().get(0);
                                existingSelectedRestaurant.getReference().update(
                                        "restaurantId", restaurantId,
                                        "dateSelected", dateDeJour
                                ).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Log.d("MyApp", "updateSelectedRestaurantUseCase réussi.");
                                        restaurantUpdated.setValue(true);
                                    } else {
                                        Log.e("MyApp", "Erreur lors de la mise à jour de la sélection existante", updateTask.getException());
                                        restaurantUpdated.setValue(false);
                                    }
                                });
                            } else {
                                // Directly create new selected restaurant if none existed
                                createNewSelectedRestaurant(restaurantId, userId, dateDeJour);
                            }
                        } else {
                            Log.e("MyApp", "Erreur lors de l'exécution de checkIfRestaurantSelectedUseCase", task.getException());
                        }
                    });
        }
    }

    private void createNewSelectedRestaurant(String restaurantId, String userId, String dateDeJour) {
        createNewSelectedRestaurantUseCase.execute(restaurantId, userId, dateDeJour)
                .addOnCompleteListener(createTask -> {
                    if (createTask.isSuccessful()) {
                        Log.d("MyApp", "createNewSelectedRestaurantUseCase réussi.");
                        restaurantCreated.setValue(true);
                    } else {
                        Log.e("MyApp", "Erreur lors de la création d'une nouvelle sélection", createTask.getException());
                        restaurantCreated.setValue(false);
                    }
                });
    }



}






