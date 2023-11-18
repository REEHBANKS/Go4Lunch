package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetUserChosenRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;
public class WorkmatesViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserChosenRestaurantsUseCase getUserChosenRestaurantsUseCase;

    // LiveData to notify the UI of changes
    private final MutableLiveData<List<UserAndPictureWithYourSelectedRestaurant>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public WorkmatesViewModel() {
        createUserUseCase = Injector.provideCreateUserUseCase();
        getUserChosenRestaurantsUseCase =Injector.provideGetUserChosenRestaurantsUseCase();
    }

    // Getter for users LiveData
    public LiveData<List<UserAndPictureWithYourSelectedRestaurant>> getUsers() {
        return usersLiveData;
    }

    // Getter for error LiveData
    public LiveData <String> getError() {
        return errorLiveData;
    }

    // Method to create a new user
    public void createUser(){
        createUserUseCase.execute();
    }

    // Method to fetch the list of users and their chosen restaurants
    public void fetchUserChosenRestaurants() {
        getUserChosenRestaurantsUseCase.execute(new UseCaseCallback<List<UserAndPictureWithYourSelectedRestaurant>>() {
            @Override
            public void onSuccess(List<UserAndPictureWithYourSelectedRestaurant> result) {
                usersLiveData.setValue(result);
            }

            @Override
            public void onError(Throwable error) {
                errorLiveData.setValue("Error fetching data: " + error.getMessage());
            }
        });
    }
}


