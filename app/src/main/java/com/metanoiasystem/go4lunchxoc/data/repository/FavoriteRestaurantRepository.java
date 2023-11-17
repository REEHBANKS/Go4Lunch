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

    // Singleton instance of the repository.
    private static volatile FavoriteRestaurantRepository instance;

    // Firebase collection names.
    private static final String RESTAURANTS_COLLECTION = "restaurants";
    private static final String FAVORITE_RESTAURANTS_SUBCOLLECTION = "favoriteRestaurants";

    // Private constructor for singleton pattern.
    private FavoriteRestaurantRepository() {
    }

    // Singleton instance getter with double-checked locking.
    public static FavoriteRestaurantRepository getInstance() {
        FavoriteRestaurantRepository result = instance;
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

    // Returns a reference to the favorite restaurants sub-collection in Firestore.
    private CollectionReference getFavoriteRestaurantCollection() {
        return FirebaseFirestore.getInstance()
                .collection(RESTAURANTS_COLLECTION)
                .document("favorites")
                .collection(FAVORITE_RESTAURANTS_SUBCOLLECTION);
    }

    // Creates a new favorite restaurant entry in Firestore.
    public Task<Void> createFavoriteRestaurant(String restaurantID) {
        FirebaseUser user = getCurrentUser();
        if (user == null) {
            Log.e("FavoriteRestaurantRepo", "Attempt to create favorite restaurant without logged-in user.");
            return Tasks.forException(new Exception("No user connected")); // Create a failed task
        }

        String userId = user.getUid();
        FavoriteRestaurant favoriteRestaurantToCreate = new FavoriteRestaurant(restaurantID, userId);
        return this.getFavoriteRestaurantCollection().document(restaurantID).set(favoriteRestaurantToCreate);
    }

    // Gets the current Firebase user, if any.
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
