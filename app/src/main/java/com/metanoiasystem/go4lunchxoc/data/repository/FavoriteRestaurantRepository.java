package com.metanoiasystem.go4lunchxoc.data.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.metanoiasystem.go4lunchxoc.data.models.FavoriteRestaurant;

public class FavoriteRestaurantRepository {

    private static volatile  FavoriteRestaurantRepository instance;


    //Collections
    private static final String RESTAURANTS_COLLECTION = "restaurants";
    private static final String FAVORITE_RESTAURANTS_SUBCOLLECTION = "favoriteRestaurants";

    private FavoriteRestaurantRepository () {
    }

    public static FavoriteRestaurantRepository  getInstance() {
        FavoriteRestaurantRepository  result = instance;
        if (result != null) {
            return result;
        }
        synchronized (FavoriteRestaurantRepository.class) {
            if (instance == null) {
                instance = new FavoriteRestaurantRepository();
            }
            return instance;
        }
    }

    // Get Collection References
    private CollectionReference getFavoriteRestaurantCollection() {
        return FirebaseFirestore.getInstance()
                .collection(RESTAURANTS_COLLECTION)
                .document("favorites")
                .collection(FAVORITE_RESTAURANTS_SUBCOLLECTION);
    }

    // Create Favorite restaurant in Firestore

    public Task<Void> createFavoriteRestaurant(String restaurantID) {
        FirebaseUser user = getCurrentUser();
        if (user == null) {
            Log.e("FavoriteRestaurantRepo", "Tentative de création d'un restaurant favori sans utilisateur connecté.");
            return Tasks.forException(new Exception("No user connected")); // Create a failed task
        }

        String userId = user.getUid();
        FavoriteRestaurant favoriteRestaurantToCreate = new FavoriteRestaurant(restaurantID, userId);
        return this.getFavoriteRestaurantCollection().document(restaurantID).set(favoriteRestaurantToCreate);
    }


    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


}
