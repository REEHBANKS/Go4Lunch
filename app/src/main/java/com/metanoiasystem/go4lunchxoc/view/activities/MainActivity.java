package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.view.fragments.ListRestaurantsFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.MapFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.WorkmatesFragment;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private Fragment mMapFragment, mListRestaurantFragment, mWorkmatesFragment;
    private int selectedFragment = R.id.maps_view; // valeur par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restaurer l'ID du fragment sélectionné à partir de l'état sauvegardé
            selectedFragment = savedInstanceState.getInt("selectedFragment");
        }

        // Initialisation des fragments
        mMapFragment = new MapFragment();
        mListRestaurantFragment = new ListRestaurantsFragment();
        mWorkmatesFragment = new WorkmatesFragment();

        // Mettre en place les fragments initiaux
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mMapFragment, "mapFragment")
                .add(R.id.container, mListRestaurantFragment, "listFragment")
                .add(R.id.container, mWorkmatesFragment, "workmatesFragment")
                .hide(mListRestaurantFragment)
                .hide(mWorkmatesFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Restaure le fragment sélectionné après une rotation ou un changement de configuration
        switchToFragment(selectedFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switchToFragment(item.getItemId());
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void switchToFragment(int fragmentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mMapFragment).hide(mListRestaurantFragment).hide(mWorkmatesFragment);

        switch (fragmentId) {
            case R.id.maps_view:
                transaction.show(mMapFragment);
                break;
            case R.id.list_view:
                transaction.show(mListRestaurantFragment);
                break;
            case R.id.workmates:
                transaction.show(mWorkmatesFragment);
                break;
        }

        transaction.commit();
        selectedFragment = fragmentId;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Sauvegarder l'ID du fragment actuellement sélectionné
        outState.putInt("selectedFragment", selectedFragment);
    }
}
