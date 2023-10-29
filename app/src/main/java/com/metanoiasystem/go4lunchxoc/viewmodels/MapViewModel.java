package com.metanoiasystem.go4lunchxoc.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantFromSearchBarUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetUserChosenRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseFetchOneRestaurantCallback;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class MapViewModel extends ViewModel {

    private final FetchRestaurantListUseCase fetchRestaurantListUseCase;
    private final GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase;
    private final GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase;
    private final FetchRestaurantFromSearchBarUseCase fetchRestaurantFromSearchBarUseCase;



    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SelectedRestaurant>> selectedRestaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> oneRestaurantLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private final MediatorLiveData<CombinedResult> combinedLiveData = new MediatorLiveData<>();

    public MapViewModel() {
        fetchRestaurantListUseCase =Injector.provideFetchRestaurantListUseCase();
        getAllSelectedRestaurantsUseCase =Injector.provideGetAllSelectedRestaurantsUseCase();
        getAllRestaurantsFromFirebaseUseCase= Injector.provideGetAllRestaurantsFromFirebaseUseCase();
        fetchRestaurantFromSearchBarUseCase = Injector.provideFetchRestaurantFromSearchBarUseCase();




        combinedLiveData.addSource(restaurantsLiveData, restaurants -> combineData());
        combinedLiveData.addSource(selectedRestaurantsLiveData, selectedRestaurants -> combineData());
    }

    String dateDeJour = new SimpleDateFormat("dd/MM/yy").format(new Date());



   public LiveData<Restaurant> getOneRestaurantLiveData() {return oneRestaurantLiveData; }


    public LiveData<Throwable> getError() {
        return errorLiveData;
    }

    public LiveData<CombinedResult> getCombinedLiveData() {
        return combinedLiveData;
    }

    private void combineData() {
        List<Restaurant> restaurants = restaurantsLiveData.getValue();
        List<SelectedRestaurant> selectedRestaurants = selectedRestaurantsLiveData.getValue();
        if (restaurants != null && selectedRestaurants != null) {
            combinedLiveData.setValue(new CombinedResult(restaurants, selectedRestaurants));
        }
    }

    public void fetchRestaurants(Double latitude, Double longitude) {
        fetchRestaurantListUseCase.execute(latitude, longitude, new UseCaseCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                restaurantsLiveData.setValue(result);

            }

            @Override
            public void onError(Throwable error) {
                Log.e("MapViewModelder", "Error fetching restaurants: " + error.getMessage());
                errorLiveData.setValue(error);
            }
        });
    }

    public void fetchAllSelectedRestaurants() {
        getAllSelectedRestaurantsUseCase.execute(dateDeJour, new UseCaseCallback<List<SelectedRestaurant>>() {
            @Override
            public void onSuccess(List<SelectedRestaurant> result) {
                Log.d("LiveDataDebu", "Selected Restaurants fetched successfully");
                // Créez une nouvelle instance de la liste pour forcer une mise à jour du LiveData
                List<SelectedRestaurant> updatedList = new ArrayList<>(result);
                selectedRestaurantsLiveData.setValue(updatedList);
            }

            @Override
            public void onError(Throwable error) {
                Log.e("LiveDataDebug", "Error fetching selected restaurants: " + error.getMessage());
                errorLiveData.setValue(error);
            }
        });
    }


    public static class CombinedResult {
        public final List<Restaurant> allRestaurants;
        public final List<SelectedRestaurant> selectedRestaurants;

        public CombinedResult(List<Restaurant> allRestaurants, List<SelectedRestaurant> selectedRestaurants) {
            this.allRestaurants = allRestaurants;
            this.selectedRestaurants = selectedRestaurants;
        }
    }




    public void getOneRestaurant(LatLng latLng, String id, Float rating,RestaurantCallback callback){
       fetchRestaurantFromSearchBarUseCase.execute(latLng, id, rating, new UseCaseFetchOneRestaurantCallback<Restaurant>() {
           @Override
           public void onSuccess(Restaurant result) {
               Log.d("ONEMAP ", "Nom du restaurant reçu dans la mapViewModel: " + result.getRestaurantName());
               callback.onRestaurantReceived(result);
           }

           @Override
           public void onError(Throwable error) {
               callback.onError(new Exception("Erreur lors de la récupération du restaurant"));

           }
       });



    }

}
