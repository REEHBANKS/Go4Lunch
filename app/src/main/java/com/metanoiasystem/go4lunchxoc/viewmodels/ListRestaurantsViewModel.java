package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantsWithSelectedUsersUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.FetchRestaurantsWithSelectedUsersUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListRestaurantsViewModel extends ViewModel {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebase;
    private final FetchRestaurantsWithSelectedUsersUseCase fetchRestaurantsWithSelectedUsersUseCase;


    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SelectedRestaurant>> selectedRestaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantWithNumberUser>> restaurantsWithNumberUserLiveData = new MutableLiveData<>();


    public ListRestaurantsViewModel(FetchRestaurantListUseCase fetchRestaurantListUseCase,
                                    GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase,
    GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebase) {
        this.fetchRestaurantListUseCase = fetchRestaurantListUseCase;
        this.getAllSelectedRestaurantsUseCase = getAllSelectedRestaurantsUseCase;
        this.getAllRestaurantsFromFirebase = getAllRestaurantsFromFirebase;
        fetchRestaurantsWithSelectedUsersUseCase = Injector.provideFetchRestaurantsWithSelectedUsersUseCase();
    }

    String dateDeJour = new SimpleDateFormat("dd/MM/yy").format(new Date());

    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    public LiveData<List<RestaurantWithNumberUser>> getRestaurantWithNumberUser(){
        return restaurantsWithNumberUserLiveData;
    }

    public LiveData<List<SelectedRestaurant>> getSelectedRestaurants() {
        return selectedRestaurantsLiveData;
    }


    public LiveData<Throwable> getError() {
        return errorLiveData;
    }

    public void fetchRestaurants(Double latitude, Double longitude) {
        fetchRestaurantListUseCase.execute(latitude, longitude, new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantsLiveData.setValue(result);
                Log.d("ListViewModel", "Successfully fetched " + result.size() + " restaurants.");
            }

            @Override
            public void onError(Throwable error) {
                errorLiveData.setValue(error);
            }
        });
    }

    public void setGetAllSelectedRestaurantsUseCase() {
            getAllRestaurantsFromFirebase.execute(new UseCaseCallback<List<Restaurant>>(){

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

    public void fetchRestaurantsWithSelectedUsers() {
        fetchRestaurantsWithSelectedUsersUseCase.execute(new UseCaseCallback<List<RestaurantWithNumberUser>>() {
            @Override
            public void onSuccess(List<RestaurantWithNumberUser> result) {

                List<RestaurantWithNumberUser>updateList = new ArrayList<>(result);

                restaurantsWithNumberUserLiveData.setValue(updateList);
            }

            @Override
            public void onError(Throwable error) {
                // Gérer l'erreur ici, par exemple en affichant un message à l'utilisateur ou en mettant à jour un autre LiveData avec l'erreur.
            }
        });
    }



    public void fetchAllSelectedRestaurants() {
        getAllSelectedRestaurantsUseCase. execute(dateDeJour, new UseCaseCallback<List<SelectedRestaurant>>() {
            @Override
            public void onSuccess(List<SelectedRestaurant> result) {
                selectedRestaurantsLiveData.setValue(result);
            }

            @Override
            public void onError(Throwable error) {
                errorLiveData.setValue(error);
            }
        });
    }
}
