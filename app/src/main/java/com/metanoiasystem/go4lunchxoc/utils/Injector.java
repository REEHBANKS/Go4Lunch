package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;

public class Injector {

    private static UserRepository userRepository = null;
    private static CreateUserUseCase createUserUseCase = null;

    public static UserRepository provideUserRepository() {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance();
        }
        return userRepository;
    }

    public static CreateUserUseCase provideCreateUserUseCase() {
        if (createUserUseCase == null) {
            createUserUseCase = new CreateUserUseCase(provideUserRepository());
        }
        return createUserUseCase;
    }
}

