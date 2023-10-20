package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchOneRestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseFetchOneRestaurantCallback;

public class FetchRestaurantFromSearchBarUseCase {

    private final RestaurantRepository restaurantRepository;

    public FetchRestaurantFromSearchBarUseCase(){
        restaurantRepository = Injector.provideRestaurantRepository();
    }



    public void execute(LatLng latLng, String id, Float rating, UseCaseFetchOneRestaurantCallback<Restaurant> callback) {
        Log.d("DEBUG", "execute called in UseCase");

        restaurantRepository.fetchOneRestaurantFromNetwork(latLng, id,rating, new RepositoryFetchOneRestaurantCallback<Restaurant>() {

            @Override
            public void onSuccess(Restaurant restaurant) {
                Log.d("DEBUG", "Restaurant fetched in UseCase");
                callback.onSuccess(restaurant);


            }

            @Override
            public void onError(Throwable error) {
                Log.e("DEBUG", "Error fetching restaurant in UseCase", error);
                callback.onError(error);
            }
        });
    }





}
