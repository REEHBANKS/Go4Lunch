package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

public class ListRestaurantsFragment extends Fragment {

    private MapViewModel mapViewModel;
    double mapLatitude = 49.1479317;
    double mapLongitude = 2.2466113;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        mapViewModel.getMapLiveData().observe(this, restaurants -> {
            for (Restaurant restaurant : restaurants) {
                Log.d("MY_TAG", "Restaurant: " + restaurant.toString());
            }
        });

        mapViewModel.fetchMapViewModel(mapLatitude, mapLongitude);


        return inflater.inflate(R.layout.fragment_list_restaurants, container, false);
    }

}