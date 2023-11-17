package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.UpdateExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public class HandleExistingSelectionUseCaseImpl implements HandleExistingSelectionUseCase {
    // Use cases for updating an existing restaurant selection and creating a new one.
    private final UpdateExistingRestaurantSelectionUseCase updateExistingRestaurantSelectionUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;

    // Constructor initializing with required use cases.
    public HandleExistingSelectionUseCaseImpl(UpdateExistingRestaurantSelectionUseCase updateExistingRestaurantSelectionUseCase,
                                              CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase) {
        this.updateExistingRestaurantSelectionUseCase = updateExistingRestaurantSelectionUseCase;
        this.createNewSelectedRestaurantUseCase = createNewSelectedRestaurantUseCase;
    }

    // Executes the use case to either update an existing restaurant selection or create a new one.
    @Override
    public void execute(String restaurantId, String userId, String dateDeJour, Task<QuerySnapshot> task, SelectedUseCaseCallback callback) {
        QuerySnapshot querySnapshot = task.getResult();
        // Check if there is an existing selection.
        if (querySnapshot != null && !querySnapshot.isEmpty()) {
            // Update existing selection.
            updateExistingRestaurantSelectionUseCase.execute(restaurantId, dateDeJour, querySnapshot);
        } else {
            // Create a new selection.
            createNewSelectedRestaurantUseCase.execute(restaurantId, userId, dateDeJour, callback);
        }
    }
}

