package com.metanoiasystem.go4lunchxoc.domain.usecase;


import android.util.Log;

import com.metanoiasystem.go4lunchxoc.utils.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public class CheckAndHandleExistingRestaurantSelectionUseCaseImpl implements CheckAndHandleExistingRestaurantSelectionUseCase {
    private final CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase;
    private final HandleExistingSelectionUseCase handleExistingSelectionUseCase;

    public CheckAndHandleExistingRestaurantSelectionUseCaseImpl(CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase, HandleExistingSelectionUseCase handleExistingSelectionUseCase) {
        this.checkIfRestaurantSelectedUseCase = checkIfRestaurantSelectedUseCase;
        this.handleExistingSelectionUseCase = handleExistingSelectionUseCase;
    }

    @Override
    public void execute(String restaurantId, String userId, String dateDeJour, SelectedUseCaseCallback callback) {
        checkIfRestaurantSelectedUseCase.execute(userId, dateDeJour)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        handleExistingSelectionUseCase.execute(restaurantId,userId, dateDeJour, task, callback);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }
}
