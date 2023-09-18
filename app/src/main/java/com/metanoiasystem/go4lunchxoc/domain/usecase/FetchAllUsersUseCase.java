package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;

public class FetchAllUsersUseCase {

    private final UserRepository userRepository;

    public FetchAllUsersUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Task<QuerySnapshot> execute() {
        return userRepository.getAllUsersData();
    }
}
