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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetUserChosenRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.NavigationDrawerHandler;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;
import com.metanoiasystem.go4lunchxoc.view.fragments.ListRestaurantsFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.MapFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.WorkmatesFragment;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;



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

        mapViewModelToMain = new ViewModelProvider(this).get(MapViewModel.class);

        navigationDrawerHandler = new NavigationDrawerHandler(this);


        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_top_menu, menu);

        // Afficher le bouton de tri uniquement pour mListRestaurantFragment
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setVisible(selectedFragment == R.id.list_view);

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
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Sauvegarder l'ID du fragment actuellement sélectionné
        outState.putInt("selectedFragment", selectedFragment);
    }

    // Gestion des sélections d'éléments du menu d'options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();  // Revenir à l'écran principal
            } else {
                navigationDrawerHandler.openNavigationDrawer();  // Ouvrir le tiroir de navigation
            }

            return true;

        }

        // Gérer l'action de tri
        if (item.getItemId() == R.id.action_sort) {
            // Vérifiez si mListRestaurantFragment est visible
            if (selectedFragment == R.id.list_view && mListRestaurantFragment != null) {
                ((ListRestaurantsFragment) mListRestaurantFragment).showSortMenu(); // Vous devez passer une vue ici si nécessaire
            }
            return true;
        }



        // Gérer l'action de recherche
        if (item.getItemId() == R.id.action_search) {
            // Récupérez la latitude et la longitude stockées
            SharedPreferences sharedPreferences = getSharedPreferences("location_prefs", Context.MODE_PRIVATE);
            double storedLatitude = sharedPreferences.getFloat("latitude", Float.MIN_VALUE);
            double storedLongitude = sharedPreferences.getFloat("longitude", Float.MIN_VALUE);

            // Vérifiez si vous avez des coordonnées valides
            if (storedLatitude != Float.MIN_VALUE && storedLongitude != Float.MIN_VALUE) {
                LatLng center = new LatLng(storedLatitude, storedLongitude);
                double radiusInMeters = 1500;
                // Convertissez le rayon en degrés pour une utilisation avec RectangularBounds
                double radiusInDegrees = radiusInMeters / 111320f;

                // Créez les limites rectangulaires
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(center.latitude - radiusInDegrees, center.longitude - radiusInDegrees),
                        new LatLng(center.latitude + radiusInDegrees, center.longitude + radiusInDegrees)
                );

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setLocationBias(bounds)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            } else {
                // Gérez le cas où les coordonnées ne sont pas disponibles
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (mWorkmatesFragment.isVisible()) {
            Toast.makeText(this, "NOT AVAILABLE", Toast.LENGTH_SHORT).show();
        }



        if (actionBarDrawerToggle != null && actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Gestion du résultat de l'intention d'AutoComplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                mapViewModelToMain.getOneRestaurant(place.getLatLng(), place.getId(), Objects.requireNonNull(place.getRating()).floatValue(), new RestaurantCallback() {
                    @Override
                    public void onRestaurantReceived(Restaurant restaurant) {
                        // Vérifiez quel fragment est actuellement visible
                        if (mMapFragment.isVisible()) {
                            // Si c'est le MapFragment, ajoutez un marqueur
                            ((MapFragment) mMapFragment).addSearchRestaurantMarker(restaurant);
                        } else if (mListRestaurantFragment.isVisible()) {
                            // Si c'est le ListRestaurantFragment, lancez RestaurantDetailActivity
                            // Vous devrez peut-être adapter cette partie pour créer un RestaurantWithNumberUser si nécessaire
                            RestaurantWithNumberUser restaurantWithNumberUser = new RestaurantWithNumberUser(restaurant, 0); // Supposons que 0 est le nombre d'utilisateurs par défaut
                            ((ListRestaurantsFragment) mListRestaurantFragment).launchRestaurantDetailActivity(restaurantWithNumberUser);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MainActivity", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}