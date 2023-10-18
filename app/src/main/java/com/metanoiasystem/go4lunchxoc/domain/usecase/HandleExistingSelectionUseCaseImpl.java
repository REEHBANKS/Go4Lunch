package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.utils.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.UpdateExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public class HandleExistingSelectionUseCaseImpl implements HandleExistingSelectionUseCase {
    private final UpdateExistingRestaurantSelectionUseCase updateExistingRestaurantSelectionUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;

    public HandleExistingSelectionUseCaseImpl(UpdateExistingRestaurantSelectionUseCase updateExistingRestaurantSelectionUseCase, CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase) {
        this.updateExistingRestaurantSelectionUseCase = updateExistingRestaurantSelectionUseCase;
        this.createNewSelectedRestaurantUseCase = createNewSelectedRestaurantUseCase;
    }



    @Override
    public void execute(String restaurantId, String userId, String dateDeJour, Task<QuerySnapshot> task, SelectedUseCaseCallback callback) {
        QuerySnapshot querySnapshot = task.getResult();
        if (querySnapshot != null && !querySnapshot.isEmpty()) {
            updateExistingRestaurantSelectionUseCase.execute(restaurantId, dateDeJour, querySnapshot);
        } else {
            createNewSelectedRestaurantUseCase.execute(restaurantId, userId, dateDeJour, callback);

        }
    }


}
