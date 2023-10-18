package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.UpdateExistingRestaurantSelectionUseCase;

public class UpdateExistingRestaurantSelectionUseCaseImpl implements UpdateExistingRestaurantSelectionUseCase {
    private final SelectedRestaurantRepository selectedRestaurantRepository;

    public UpdateExistingRestaurantSelectionUseCaseImpl(SelectedRestaurantRepository selectedRestaurantRepository) {
        this.selectedRestaurantRepository = selectedRestaurantRepository;
    }

    @Override
    public Task<Void> execute(String restaurantId, String dateDeJour, QuerySnapshot querySnapshot) {
        DocumentSnapshot existingSelectedRestaurant = querySnapshot.getDocuments().get(0);
        return selectedRestaurantRepository.updateSelectedRestaurant(existingSelectedRestaurant.getReference(), restaurantId, dateDeJour);
    }
}
