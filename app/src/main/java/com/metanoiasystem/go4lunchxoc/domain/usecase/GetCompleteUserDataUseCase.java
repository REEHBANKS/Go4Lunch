package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;


public class GetCompleteUserDataUseCase {

    // Repository for handling user data.
    private final UserRepository userRepository;

    // Constructor initializing the use case with a user repository.
    public GetCompleteUserDataUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Executes the use case to fetch complete user data.
    public void execute(CallbackUserUseCase callback) {
        userRepository.getUserData().addOnSuccessListener(userDocument -> {
            if (userDocument != null && userDocument.exists()) {
                // Convert the document snapshot to a User object.
                User user = userDocument.toObject(User.class);
                // Callback with the fetched user data.
                callback.onUserDataFetched(user);
            } else {
                // Handle cases where user data is not found.
                callback.onUserDataFetched(null);
            }
        }).addOnFailureListener(callback::onError); // Handle any errors during data fetch.
    }
}


