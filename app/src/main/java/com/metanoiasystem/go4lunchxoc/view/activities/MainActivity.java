package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  private MapViewModel mapViewModel;
    double mapLatitude = 49.1479317;
    double mapLongitude = 2.2466113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        mapViewModel.getMapLiveData().observe(this, restaurants -> {
            for (Restaurant restaurant : restaurants) {
                Log.d("MY_TAG", "Restaurant: " + restaurant.toString());
            }
        });

        mapViewModel.fetchMapViewModel(mapLatitude, mapLongitude);


        }


}