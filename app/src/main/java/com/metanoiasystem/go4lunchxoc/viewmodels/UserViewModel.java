package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;

import java.util.List;
public class UserViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;

    // LiveData poantSelectorLisur notifier l'UI des changements
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UserViewModel(CreateUserUseCase createUserUseCase, FetchAllUsersUseCase fetchAllUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.fetchAllUsersUseCase = fetchAllUsersUseCase;
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }

    public LiveData <String> getError() {
        return errorLiveData;
    }


    public void createUser(){
        createUserUseCase.execute();
    }

    public void fetchAllUsers() {
        fetchAllUsersUseCase.execute().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> users = task.getResult().toObjects(User.class);
                usersLiveData.setValue(users);
                Log.d("UserViewModel", "Successfully fetched " + users.size() + " users.");
            } else {
                errorLiveData.setValue("Error fetching users.");
                Log.e("UserViewModel", "Error fetching users.", task.getException());
            }

        });
    }
}

