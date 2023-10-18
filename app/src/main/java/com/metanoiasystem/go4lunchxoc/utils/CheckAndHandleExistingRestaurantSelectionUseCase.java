package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public interface CheckAndHandleExistingRestaurantSelectionUseCase {
    void execute(String restaurantId, String userId, String dateDeJour, SelectedUseCaseCallback callback);
}

