package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetUserChosenRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;
public class WorkmatesViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;
    private final GetUserChosenRestaurantsUseCase getUserChosenRestaurantsUseCase;

    // LiveData poantSelectorLisur notifier l'UI des changements
    private final MutableLiveData<List<UserAndPictureWithYourSelectedRestaurant>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public WorkmatesViewModel() {
        createUserUseCase = Injector.provideCreateUserUseCase();
        fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();
        getUserChosenRestaurantsUseCase =Injector.provideGetUserChosenRestaurantsUseCase();
    }

    public LiveData<List<UserAndPictureWithYourSelectedRestaurant>> getUsers() {
        return usersLiveData;
    }

    public LiveData <String> getError() {
        return errorLiveData;
    }


    public void createUser(){
        createUserUseCase.execute();
    }

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

