package com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase;

import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public interface CheckAndHandleExistingRestaurantSelectionUseCase {
    void execute(String restaurantId, String userId, String dateDeJour, SelectedUseCaseCallback callback);
}

