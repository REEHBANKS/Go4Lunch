package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;

import java.util.List;
public class WorkmatesViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;

    // LiveData poantSelectorLisur notifier l'UI des changements
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public WorkmatesViewModel() {
        createUserUseCase = Injector.provideCreateUserUseCase();
        fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();
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

            } else {
                errorLiveData.setValue("Error fetching users.");

            }

        });
    }
}

