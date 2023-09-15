package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;

public class UserViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;

    public UserViewModel(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    public void createUser(){
        createUserUseCase.execute();
    }


}
