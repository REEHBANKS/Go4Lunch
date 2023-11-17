package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.UpdateExistingRestaurantSelectionUseCase;

public class UpdateExistingRestaurantSelectionUseCaseImpl implements UpdateExistingRestaurantSelectionUseCase {
    // Repository for handling operations related to selected restaurants.
    private final SelectedRestaurantRepository selectedRestaurantRepository;

    // Constructor initializing with the selected restaurant repository.
    public UpdateExistingRestaurantSelectionUseCaseImpl(SelectedRestaurantRepository selectedRestaurantRepository) {
        this.selectedRestaurantRepository = selectedRestaurantRepository;
    }

    // Executes the use case to update an existing restaurant selection.
    @Override
    public Task<Void> execute(String restaurantId, String dateDeJour, QuerySnapshot querySnapshot) {
        // Get the first document from the QuerySnapshot, which represents the existing selection.
        DocumentSnapshot existingSelectedRestaurant = querySnapshot.getDocuments().get(0);
        // Update the existing selected restaurant with new data.
        return selectedRestaurantRepository.updateSelectedRestaurant(existingSelectedRestaurant.getReference(), restaurantId, dateDeJour);
    }
}

