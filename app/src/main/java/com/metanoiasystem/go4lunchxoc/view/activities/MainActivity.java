package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.NavigationDrawerHandler;
import com.metanoiasystem.go4lunchxoc.view.fragments.ListRestaurantsFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.MapFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.WorkmatesFragment;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory.RestaurantViewModelFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private Fragment mMapFragment, mListRestaurantFragment, mWorkmatesFragment;
    private int selectedFragment = R.id.maps_view;
     MapViewModel mapViewModelToMain;
    private NavigationDrawerHandler navigationDrawerHandler;


    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchRestaurantListUseCase fetchRestaurantListUseCase = Injector.provideFetchRestaurantListUseCase();
        GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase = Injector.provideGetAllSelectedRestaurantsUseCase();
        GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase = Injector.provideGetAllRestaurantsFromFirebaseUseCase();

        RestaurantViewModelFactory factory = new RestaurantViewModelFactory(fetchRestaurantListUseCase,
                getAllSelectedRestaurantsUseCase, getAllRestaurantsFromFirebaseUseCase);
        mapViewModelToMain = new ViewModelProvider(this, factory).get(MapViewModel.class);


        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.RR_KEY);
        }

        navigationDrawerHandler = new NavigationDrawerHandler(this);
        navigationDrawerHandler.setupNavigationDrawer();
    }



    // Gonflage du menu d'options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_top_menu, menu);
        return true;
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

    // Gestion des sélections d'éléments du menu d'options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gérer l'ouverture du tiroir de navigation
        if (item.getItemId() == android.R.id.home) {
            navigationDrawerHandler.openDrawer();
            return true;
        }

        // Gérer l'action de recherche
        if (item.getItemId() == R.id.action_search) {
            if (mMapFragment.isVisible()) {
                // ...
            } else if (mListRestaurantFragment.isVisible()) {
                // ...
            } else if (mWorkmatesFragment.isVisible()) {
                Toast.makeText(this, "NOT AVAILABLE", Toast.LENGTH_SHORT).show();
            }
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            return true;
        }

        // Si l'actionBarDrawerToggle gère l'item, retournez true
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Gestion du résultat de l'intention d'AutoComplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("DEBUG", "onActivityResult called with requestCode: " + requestCode + ", resultCode: " + resultCode);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE)
            Log.d("DEBUG", "Inside AUTOCOMPLETE_REQUEST_CODE block");{
            if (resultCode == RESULT_OK) {
                Log.d("DEBUG", "Result OK for AUTOCOMPLETE_REQUEST_CODE");
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                mapViewModelToMain.fetchOneMapViewModel(place.getLatLng(), place.getId(),Objects.requireNonNull(place.getRating()).floatValue());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MainActivity", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}