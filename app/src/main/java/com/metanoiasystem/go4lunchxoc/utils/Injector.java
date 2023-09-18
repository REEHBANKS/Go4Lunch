package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;

public class Injector {

    private static UserRepository userRepository = null;
    private static CreateUserUseCase createUserUseCase = null;
    private static FetchAllUsersUseCase fetchAllUsersUseCase = null;

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

    public static FetchAllUsersUseCase provideFetchAllUsersUseCase() {
        if (fetchAllUsersUseCase == null) {
            fetchAllUsersUseCase = new FetchAllUsersUseCase((provideUserRepository()));
        }
        return fetchAllUsersUseCase;

    }
}

