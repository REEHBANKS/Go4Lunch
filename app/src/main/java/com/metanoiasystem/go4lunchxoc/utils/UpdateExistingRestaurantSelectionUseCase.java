package com.metanoiasystem.go4lunchxoc.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface UpdateExistingRestaurantSelectionUseCase {
    Task<Void> execute(String restaurantId, String dateDeJour, QuerySnapshot querySnapshot);
}
