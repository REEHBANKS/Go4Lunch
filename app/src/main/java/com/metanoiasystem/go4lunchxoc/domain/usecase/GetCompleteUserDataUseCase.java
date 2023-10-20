package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;

public class GetCompleteUserDataUseCase {

    private final UserRepository userRepository;

    public GetCompleteUserDataUseCase(){
        userRepository = Injector.provideUserRepository();
    }

    public void execute( CallbackUserUseCase callback) {
        userRepository.getUserData().addOnSuccessListener(userDocument -> {
            if (userDocument != null && userDocument.exists()) {
                User user = userDocument.toObject(User.class);
                callback.onUserDataFetched(user);
            } else {
                callback.onUserDataFetched(null);
            }
        }).addOnFailureListener(callback::onError);
    }
}

