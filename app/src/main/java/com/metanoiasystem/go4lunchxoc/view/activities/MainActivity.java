package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.utils.NavigationDrawerHandler;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RestaurantCallback;
import com.metanoiasystem.go4lunchxoc.view.fragments.ListRestaurantsFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.MapFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.WorkmatesFragment;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



public class MainActivity extends AppCompatActivity {

    // Fragments for different sections of the app
    private Fragment mMapFragment, mListRestaurantFragment, mWorkmatesFragment;
    private int selectedFragment = R.id.maps_view;
    private MapViewModel mapViewModelToMain;
    private NavigationDrawerHandler navigationDrawerHandler;

    // Toolbar and drawer layout for the navigation drawer
    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    // Request code for autocomplete activity
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the view model
        mapViewModelToMain = new ViewModelProvider(this).get(MapViewModel.class);

        // Setup navigation drawer handler
        navigationDrawerHandler = new NavigationDrawerHandler(this);

        // Setup the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // Restore selected fragment after configuration changes
        if (savedInstanceState != null) {
            selectedFragment = savedInstanceState.getInt("selectedFragment");
        }

        // Initialize the fragments
        mMapFragment = new MapFragment();
        mListRestaurantFragment = new ListRestaurantsFragment();
        mWorkmatesFragment = new WorkmatesFragment();

        // Setup initial fragments in the container
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mMapFragment, "mapFragment")
                .add(R.id.container, mListRestaurantFragment, "listFragment")
                .add(R.id.container, mWorkmatesFragment, "workmatesFragment")
                .hide(mListRestaurantFragment)
                .hide(mWorkmatesFragment)
                .commit();

        // Setup bottom navigation view and fragment switching
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        switchToFragment(selectedFragment);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switchToFragment(item.getItemId());
            return true;
        });

        // Initialize Places API if not already done
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.RR_KEY);
        }

        // Setup navigation drawer
        navigationDrawerHandler.setupNavigationDrawer();
    }

    // Setup options menu and visibility of sort button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_top_menu, menu);
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setVisible(selectedFragment == R.id.list_view);
        return true;
    }

    // Switch between different fragments based on navigation selection
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
        invalidateOptionsMenu(); // Refresh menu items
    }

    // Save the currently selected fragment's ID
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedFragment", selectedFragment);
    }

    // Handles selections of options menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the home/up button in the toolbar
        if (item.getItemId() == android.R.id.home) {
            // Check if there are entries in the fragment back stack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // Pop the top state off the back stack (return to previous fragment or activity)
                getSupportFragmentManager().popBackStack();
            } else {
                // Open the navigation drawer if back stack is empty
                navigationDrawerHandler.openNavigationDrawer();
            }
            return true;
        }

        // Handle action for sorting restaurants
        if (item.getItemId() == R.id.action_sort) {
            // Check if the ListRestaurantsFragment is visible
            if (selectedFragment == R.id.list_view && mListRestaurantFragment != null) {
                // Show sorting options if ListRestaurantsFragment is present
                ((ListRestaurantsFragment) mListRestaurantFragment).showSortMenu();
            }
            return true;
        }

        // Handle action for searching places
        if (item.getItemId() == R.id.action_search) {
            // Retrieve stored location coordinates
            SharedPreferences sharedPreferences = getSharedPreferences("location_prefs", Context.MODE_PRIVATE);
            double storedLatitude = sharedPreferences.getFloat("latitude", Float.MIN_VALUE);
            double storedLongitude = sharedPreferences.getFloat("longitude", Float.MIN_VALUE);

            // Check if valid coordinates are available
            if (storedLatitude != Float.MIN_VALUE && storedLongitude != Float.MIN_VALUE) {
                // Create a LatLng object for the center using the stored coordinates
                LatLng center = new LatLng(storedLatitude, storedLongitude);
                double radiusInMeters = 1500;
                // Convert the radius from meters to degrees for use with RectangularBounds
                double radiusInDegrees = radiusInMeters / 111320f;

                // Create rectangular bounds for the autocomplete search area
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(center.latitude - radiusInDegrees, center.longitude - radiusInDegrees),
                        new LatLng(center.latitude + radiusInDegrees, center.longitude + radiusInDegrees)
                );

                // Specify the fields to return from the autocomplete results
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING);
                // Build and launch the autocomplete intent with the specified bounds and fields
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setLocationBias(bounds)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            } else {
                // Display a message if location coordinates are not available
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (mWorkmatesFragment.isVisible()) {
            // Display a message if the action is not available when WorkmatesFragment is visible
            Toast.makeText(this, "NOT AVAILABLE", Toast.LENGTH_SHORT).show();
        }

        // Handle navigation drawer toggle
        if (actionBarDrawerToggle != null && actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    // Handle results from the autocomplete intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check if the result is from the autocomplete activity
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            // Handle the response based on the result code
            if (resultCode == RESULT_OK) {
                assert data != null;
                // Retrieve the selected place from the autocomplete data
                Place place = Autocomplete.getPlaceFromIntent(data);
                // Fetch details of the selected restaurant using its place ID and location
                mapViewModelToMain.getOneRestaurant(place.getLatLng(), place.getId(), Objects.requireNonNull(place.getRating()).floatValue(), new RestaurantCallback() {
                    @Override
                    public void onRestaurantReceived(Restaurant restaurant) {
                        // Determine which fragment is currently visible
                        if (mMapFragment.isVisible()) {
                            // If it's the MapFragment, add a marker for the searched restaurant
                            ((MapFragment) mMapFragment).addSearchRestaurantMarker(restaurant);
                        } else if (mListRestaurantFragment.isVisible()) {
                            // If it's the ListRestaurantFragment, launch the restaurant detail activity
                            // Create a RestaurantWithNumberUser if necessary for the detail activity
                            RestaurantWithNumberUser restaurantWithNumberUser = new RestaurantWithNumberUser(restaurant, 0); // Assuming 0 is the default number of users
                            ((ListRestaurantsFragment) mListRestaurantFragment).launchRestaurantDetailActivity(restaurantWithNumberUser);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // Handle any errors encountered during the restaurant fetch process
                    }
                });
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                // Retrieve and log the status error from the autocomplete intent
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MainActivity", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}