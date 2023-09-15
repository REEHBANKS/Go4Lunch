package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.UserViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private final CreateUserUseCase createUserUseCase;

    public UserViewModelFactory(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(createUserUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
