package com.metanoiasystem.go4lunchxoc.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;

public class SelectedRestaurantRepository {

    // Singleton instance of SelectedRestaurantRepository.
    private static volatile SelectedRestaurantRepository instance;

    // Firebase collection names.
    private static final String RESTAURANTS_COLLECTION = "restaurants";
    private static final String SELECTED_RESTAURANTS_SUBCOLLECTION = "selectedRestaurants";

    // Private constructor for singleton pattern.
    private SelectedRestaurantRepository(){}

    // Singleton instance getter with double-checked locking.
    public static SelectedRestaurantRepository getInstance() {
        SelectedRestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (SelectedRestaurantRepository.class) {
            if (instance == null) {
                instance = new SelectedRestaurantRepository();
            }
            return instance;
        }
    }

    // Returns a reference to the selected restaurants sub-collection in Firestore.
    private CollectionReference getSelectedRestaurantCollection() {
        return FirebaseFirestore.getInstance()
                .collection(RESTAURANTS_COLLECTION)
                .document("selected")
                .collection(SELECTED_RESTAURANTS_SUBCOLLECTION);
    }

    // Fetches all selected restaurants for a given date from Firestore.
    public Task<QuerySnapshot> getAllSelectedRestaurants(String dateDuJour) {
        return getSelectedRestaurantCollection()
                .whereEqualTo("dateSelected", dateDuJour)
                .get();
    }

    // Checks if a restaurant is already selected by the current user on a given date.
    public Task<QuerySnapshot> checkIfRestaurantSelected(String userId, String date) {
        return getSelectedRestaurantCollection()
                .whereEqualTo("userId", userId)
                .whereEqualTo("dateSelected", date)
                .get();
    }

    // Creates a new selected restaurant in Firestore.
    public Task<DocumentReference> createNewSelectedRestaurant(String restaurantId, String userId, String date) {
        SelectedRestaurant selectedRestaurantToCreate = new SelectedRestaurant(restaurantId, userId, date);
        return getSelectedRestaurantCollection().add(selectedRestaurantToCreate);
    }

    // Updates an existing selected restaurant in Firestore.
    public Task<Void> updateSelectedRestaurant(DocumentReference restaurantRef, String restaurantId, String date) {
        return restaurantRef.update(
                "restaurantId", restaurantId,
                "dateSelected", date
        );
    }

    // Fetches all selected restaurants with a specific ID for a given date from Firestore.
    public Task<QuerySnapshot> getAllSelectedRestaurantsWithId(String restaurantId, String dateDuJour) {
        return getSelectedRestaurantCollection()
                .whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("dateSelected", dateDuJour)
                .get();
    }
}
