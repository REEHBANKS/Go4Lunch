package com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

public interface HandleExistingSelectionUseCase {

    void execute(String restaurantId, String userId, String dateDeJour, Task<QuerySnapshot> task, SelectedUseCaseCallback callback);
}
