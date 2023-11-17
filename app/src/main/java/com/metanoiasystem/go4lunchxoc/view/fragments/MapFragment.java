package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.providers.LocationProvider;
import com.metanoiasystem.go4lunchxoc.view.activities.RestaurantDetailActivity;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment implements LocationProvider.OnLocationReceivedListener {

    private LocationProvider locationProvider;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private GoogleMap mMap;
    private MapViewModel mapViewModel;
    private Marker userMarker;
    private final List<Marker> restaurantMarkers = new ArrayList<>();
    private boolean locationRequested = false;
    private boolean isMapReady = false;
    private Restaurant pendingRestaurant = null;

    // Inflate the layout for this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // Initialize the map fragment and location provider
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationProvider = new LocationProvider(requireContext());

        // Set up the map fragment asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    // Handle location permission and update location on start
    @Override
    public void onStart() {
        super.onStart();
        // Check for location permission and request updates
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(this);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }

        // Request current location and process it
        locationProvider.requestCurrentLocation(new LocationProvider.OnLocationReceivedListener() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                // Save the current location in shared preferences
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("location_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("latitude", (float) latitude);
                editor.putFloat("longitude", (float) longitude);
                editor.apply();
                Log.d("DEBUGO", "LocationWorking.");

                // Fetch restaurants and selected restaurants based on the current location
                mapViewModel.fetchRestaurants(latitude, longitude);
                mapViewModel.fetchAllSelectedRestaurants();
            }
        });
    }



    // Handle the fragment's pause lifecycle event
    @Override
    public void onPause() {
        super.onPause();
        // Remove observers and stop location updates when the fragment is paused
        mapViewModel.getCombinedLiveData().removeObservers(getViewLifecycleOwner());
        locationProvider.stopLocationUpdates();
        userMarker = null;
    }

    // Handle the fragment's resume lifecycle event
    @Override
    public void onResume() {
        super.onResume();
        // Request location updates and observe restaurant data when the fragment resumes
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(this);
        }
        mapViewModel.getCombinedLiveData().observe(getViewLifecycleOwner(), this::updateMapWithMarkers);
        // Additional checks for selected restaurant changes can be done here
    }

    // Callback when the user's location is received
    @Override
    public void onLocationReceived(double latitude, double longitude) {
        // Update the map with the user's current location
        if (mMap != null) {
            LatLng myLocation = new LatLng(latitude, longitude);
            if (userMarker == null) {
                // Add a marker at the user's location if not already present
                userMarker = mMap.addMarker(new MarkerOptions()
                        .icon(bitmapDescriptorFactory(getContext(), R.drawable.icon_you_are_here))
                        .position(myLocation)
                        .title("You're here!"));
            } else {
                // Update the existing marker's position
                userMarker.setPosition(myLocation);
            }
            // Move the map camera to the user's location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        }
    }

    // Callback when the map is ready
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // Initialize the map and set its settings
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            checkAccessRestaurant();
            isMapReady = true;

            // Set a listener for marker click events
            mMap.setOnMarkerClickListener(clickedMarker -> {
                // Launch RestaurantDetailActivity when a restaurant marker is clicked
                Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
                intent.putExtra(RestaurantDetailActivity.RESTAURANT_KEY, (Restaurant) clickedMarker.getTag());
                startActivity(intent);
                return true;
            });

            // Add a marker for a pending restaurant if it exists
            if (pendingRestaurant != null) {
                addSearchRestaurantMarker(pendingRestaurant);
                pendingRestaurant = null;
            }

            // Check if combined restaurant data is already available and update the map
            if (mapViewModel.getCombinedLiveData().getValue() != null) {
                updateMapWithMarkers(mapViewModel.getCombinedLiveData().getValue());
            }

            // Observe combined restaurant data to update markers on the map
            observeCombinedLiveData();
        }
    };

    // Observe combined live data from the ViewModel
    private void observeCombinedLiveData() {
        mapViewModel.getCombinedLiveData().observe(getViewLifecycleOwner(), this::updateMapWithMarkers);
    }

    // Update the map with markers for restaurants
    private void updateMapWithMarkers(MapViewModel.CombinedResult result) {
        // Check if the map is ready before updating
        if (!isMapReady) return;
        // Add markers for all restaurants and selected restaurants
        addRestaurantMarkers(result.allRestaurants, result.selectedRestaurants);
    }

    // Add a marker on the map for a specific restaurant
    public void addSearchRestaurantMarker(Restaurant restaurant) {
        // Check if the map is initialized
        if (mMap == null) return;
        // Create a marker for the restaurant
        LatLng restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(bitmapDescriptorFactory(getContext(), R.drawable.icon_blue_lunch))
                .position(restaurantLocation)
                .title(restaurant.getRestaurantName()));
        marker.setTag(restaurant);
        // Move the camera to focus on the restaurant's location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 17));
    }

    // Check and request location access permissions
    private void checkAccessRestaurant() {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable showing the user's location on the map
            mMap.setMyLocationEnabled(true);
            // Request the current location if not already requested
            if (!locationRequested) {
                locationProvider.requestCurrentLocation(new LocationProvider.OnLocationReceivedListener() {
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        // Additional actions can be performed here upon receiving location
                    }
                });
                locationRequested = true;
            }
        } else {
            // Request permission if not granted
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            // Check if permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Request location updates if permission is granted
                locationProvider.requestLocationUpdates(this);
            } else {
                // Show a toast message if permission is denied
                Toast.makeText(getContext(), "Permission required to show location on map", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Method to change the icon of a map marker
    private BitmapDescriptor bitmapDescriptorFactory(Context context, int vectorResId) {
        // Create a bitmap from a vector drawable
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Add markers for a list of restaurants
    private void addRestaurantMarkers(List<Restaurant> restaurants, List<SelectedRestaurant> selectedRestaurants) {
        // Check if the map is initialized
        if (mMap == null) return;
        // Clear existing markers
        for (Marker marker : restaurantMarkers) {
            marker.remove();
        }
        restaurantMarkers.clear();

        // Create a map to quickly check if a restaurant is selected
        HashMap<String, Boolean> isSelectedMap = new HashMap<>();
        if (selectedRestaurants != null && !selectedRestaurants.isEmpty()) {
            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                isSelectedMap.put(selectedRestaurant.getRestaurantId(), true);
            }
        }

        // Add markers for all restaurants
        if (restaurants == null) {
            restaurants = new ArrayList<>();
        }
        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
            int markerIcon = isSelectedMap.containsKey(restaurant.getId()) ? R.drawable.icon_green_lunch : R.drawable.icon_red_lunch;
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFactory(getContext(), markerIcon))
                    .position(restaurantLocation)
                    .title(restaurant.getRestaurantName()));
            marker.setTag(restaurant);
            restaurantMarkers.add(marker);
        }
    }



}



