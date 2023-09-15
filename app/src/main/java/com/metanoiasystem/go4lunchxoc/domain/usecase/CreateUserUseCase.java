package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute() {
       userRepository.createUser();
    }
}
