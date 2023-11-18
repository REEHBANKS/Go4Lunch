package com.metanoiasystem.go4lunchxoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;

import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;

import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.FetchRestaurantsWithSelectedUsersUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;
import java.util.ArrayList;
import java.util.List;

public class ListRestaurantsViewModel extends ViewModel {

    // Use cases for fetching data
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;
    private final FetchRestaurantsWithSelectedUsersUseCase fetchRestaurantsWithSelectedUsersUseCase;

    // LiveData objects for observing data changes
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantWithNumberUser>> restaurantsWithNumberUserLiveData = new MutableLiveData<>();

    // Constructor initializes use cases
    public ListRestaurantsViewModel() {
        getAllRestaurantsFromFirebaseUseCase = Injector.provideGetAllRestaurantsFromFirebaseUseCase();
        fetchRestaurantsWithSelectedUsersUseCase = Injector.provideFetchRestaurantsWithSelectedUsersUseCase();
    }

    // Getters for LiveData objects
    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    public LiveData<List<RestaurantWithNumberUser>> getRestaurantWithNumberUser(){
        return restaurantsWithNumberUserLiveData;
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }

    // Method to fetch all restaurants from Firebase and update LiveData
    public void setGetAllSelectedRestaurantsUseCase() {
        getAllRestaurantsFromFirebaseUseCase.execute(new UseCaseCallback<List<Restaurant>>(){

            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantsLiveData.setValue(result);
            }

            @Override
            public void onError(Throwable error) {
                errorLiveData.setValue(error);
            }
        });
    }

    // Method to fetch restaurants with the number of users who selected them
    public void fetchRestaurantsWithSelectedUsers() {
        fetchRestaurantsWithSelectedUsersUseCase.execute(new UseCaseCallback<List<RestaurantWithNumberUser>>() {
            @Override
            public void onSuccess(List<RestaurantWithNumberUser> result) {
                List<RestaurantWithNumberUser> updateList = new ArrayList<>(result);
                restaurantsWithNumberUserLiveData.setValue(updateList);
            }

            @Override
            public void onError(Throwable error) {
                // Handle errors here, such as displaying a message to the user or updating another LiveData with the error.
            }
        });
    }
}
