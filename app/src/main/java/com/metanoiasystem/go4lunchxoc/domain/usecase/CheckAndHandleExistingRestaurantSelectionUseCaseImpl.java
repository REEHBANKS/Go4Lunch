package com.metanoiasystem.go4lunchxoc.domain.usecase;


import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;
public class CheckAndHandleExistingRestaurantSelectionUseCaseImpl implements CheckAndHandleExistingRestaurantSelectionUseCase {
    // Use case references for checking if a restaurant is selected and handling existing selections.
    private final CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase;
    private final HandleExistingSelectionUseCase handleExistingSelectionUseCase;

    // Constructor to initialize the use case with necessary dependencies.
    public CheckAndHandleExistingRestaurantSelectionUseCaseImpl(CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase,
                                                                HandleExistingSelectionUseCase handleExistingSelectionUseCase) {
        this.checkIfRestaurantSelectedUseCase = checkIfRestaurantSelectedUseCase;
        this.handleExistingSelectionUseCase = handleExistingSelectionUseCase;
    }

    // Executes the use case: checks if a restaurant is already selected, and if so, handles the existing selection.
    @Override
    public void execute(String restaurantId, String userId, String dateDeJour, SelectedUseCaseCallback callback) {
        checkIfRestaurantSelectedUseCase.execute(userId, dateDeJour)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If the restaurant is already selected, handle the existing selection.
                        handleExistingSelectionUseCase.execute(restaurantId, userId, dateDeJour, task, callback);
                    } else {
                        // In case of an error, pass the exception to the callback.
                        callback.onError(task.getException());
                    }
                });
    }
}

