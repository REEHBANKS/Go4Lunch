package com.metanoiasystem.go4lunchxoc.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;

public class SelectedRestaurantRepository {

    private static volatile SelectedRestaurantRepository instance;


    //Collections
    private static final String RESTAURANTS_COLLECTION = "restaurants";
    private static final String SELECTED_RESTAURANTS_SUBCOLLECTION = "selectedRestaurants";

    private SelectedRestaurantRepository(){}

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

    // Get Collection References
    private CollectionReference getSelectedRestaurantCollection() {
        return FirebaseFirestore.getInstance()
                .collection(RESTAURANTS_COLLECTION)
                .document("selected")
                .collection(SELECTED_RESTAURANTS_SUBCOLLECTION);
    }

    // Get all restaurant selected

    public Task<QuerySnapshot> getAllSelectedRestaurants() {
        return getSelectedRestaurantCollection().get();
    }


    // Methode pour vérifier si un restaurant est déjà sélectionné par l'utilisateur actuel
    public Task<QuerySnapshot> checkIfRestaurantSelected(String userId, String date) {
        return getSelectedRestaurantCollection()
                .whereEqualTo("userId", userId)
                .whereEqualTo("dateSelected", date)
                .get();
    }

    // Methode pour créer un nouveau restaurant sélectionné
    public Task<DocumentReference> createNewSelectedRestaurant(String restaurantId, String userId, String date) {
        SelectedRestaurant selectedRestaurantToCreate = new SelectedRestaurant(restaurantId, userId, date);
        return getSelectedRestaurantCollection().add(selectedRestaurantToCreate);
    }

    // Methode pour mettre à jour un restaurant sélectionné existant
    public Task<Void> updateSelectedRestaurant(DocumentReference restaurantRef, String restaurantId, String date) {
        return restaurantRef.update(
                "restaurantId", restaurantId,
                "dateSelected", date
        );
    }

    public Task<QuerySnapshot> getAllSelectedRestaurantsWithId(String restaurantId) {
        return getSelectedRestaurantCollection()
                .whereEqualTo("restaurantId", restaurantId)
                .get();
    }






}
