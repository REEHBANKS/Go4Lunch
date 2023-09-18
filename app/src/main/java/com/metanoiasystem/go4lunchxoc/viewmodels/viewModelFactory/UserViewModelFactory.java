package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.UserViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private final CreateUserUseCase createUserUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;

    public UserViewModelFactory(CreateUserUseCase createUserUseCase, FetchAllUsersUseCase fetchAllUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.fetchAllUsersUseCase = fetchAllUsersUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(createUserUseCase, fetchAllUsersUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
