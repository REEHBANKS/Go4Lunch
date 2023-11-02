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
    private  MapViewModel mapViewModel;
    private Marker userMarker;
    private final List<Marker> restaurantMarkers = new ArrayList<>();
    private boolean locationRequested = false;
    private boolean isMapReady = false;
    private Restaurant pendingRestaurant = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {





        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        observeOneMapLiveData();
        observeCombinedLiveData();

        return inflater.inflate(R.layout.fragment_map, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationProvider = new LocationProvider(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(this);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }

        locationProvider.requestCurrentLocation(new LocationProvider.OnLocationReceivedListener() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("location_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("latitude", (float) latitude);
                editor.putFloat("longitude", (float) longitude);
                editor.apply();

                mapViewModel.fetchRestaurants(latitude, longitude);
                mapViewModel.fetchAllSelectedRestaurants();
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        mapViewModel.getCombinedLiveData().removeObservers(getViewLifecycleOwner());
        locationProvider.stopLocationUpdates();
        userMarker = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private void observeCombinedLiveData() {
        mapViewModel.getCombinedLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!isMapReady) {
                return;
            }
            List<Restaurant> allRestaurantsTemp = result.allRestaurants;
            List<SelectedRestaurant> selectedAllRestaurantsTemp = result.selectedRestaurants;

            // Log pour vérifier si les listes sont nulles ou vides
            if (allRestaurantsTemp == null || allRestaurantsTemp.isEmpty()) {
            } else {
                Log.d("DEBUG", "allRestaurantsTemp size: " + allRestaurantsTemp.size());
            }

            if (selectedAllRestaurantsTemp == null || selectedAllRestaurantsTemp.isEmpty()) {
                Log.d("DEBUG", "selectedAllRestaurantsTemp is null or empty.");
            } else {
                Log.d("DEBUG", "selectedAllRestaurantsTemp size: " + selectedAllRestaurantsTemp.size());
            }

            if (allRestaurantsTemp != null && selectedAllRestaurantsTemp != null) {
                if (!selectedAllRestaurantsTemp.isEmpty()) {
                    addRestaurantMarkers(allRestaurantsTemp, selectedAllRestaurantsTemp);
                } else {
                    addRestaurantMarkers(allRestaurantsTemp, new ArrayList<>());
                }
            }
        });
    }



    public void observeOneMapLiveData() {
        mapViewModel.getOneRestaurantLiveData().observe(getViewLifecycleOwner(), restaurant -> {

          //      addSearchRestaurantMarker(restaurant);

        });
    }





    @Override
    public void onLocationReceived(double latitude, double longitude) {
        if (mMap != null) {
            LatLng myLocation = new LatLng(latitude, longitude);
            if (userMarker == null) {
                userMarker = mMap.addMarker(new MarkerOptions()
                        .icon(bitmapDescriptorFactory(getContext(), R.drawable.icon_you_are_here))
                        .position(myLocation)
                        .title("You're here!"));

            } else {
                userMarker.setPosition(myLocation);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        }
    }



    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            checkAccessRestaurant();
            isMapReady = true;




            // Set the marker click listener here
            mMap.setOnMarkerClickListener(clickedMarker -> {
                Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
                intent.putExtra(RestaurantDetailActivity.RESTAURANT_KEY, (Restaurant) clickedMarker.getTag());
                startActivity(intent);
                return true;
            });

            if (pendingRestaurant != null) {
                addSearchRestaurantMarker(pendingRestaurant);
                pendingRestaurant = null;
            }
            // Observez les données des restaurants une fois que la carte est prête
            observeCombinedLiveData();
        }
    };

    public void addSearchRestaurantMarker(Restaurant restaurant) {
        if (mMap == null) return;
        LatLng restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(bitmapDescriptorFactory(getContext(),  R.drawable.icon_green_lunch))
                .position(restaurantLocation)
                .title(restaurant.getRestaurantName()));
        marker.setTag(restaurant);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 17));
    }

    private void checkAccessRestaurant() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (!locationRequested) {
                locationProvider.requestCurrentLocation(new LocationProvider.OnLocationReceivedListener() {
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {

                    }

                });
                locationRequested = true;
            }
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationProvider.requestLocationUpdates(this);
            } else {
                Toast.makeText(getContext(), "Permission required to show location on map", Toast.LENGTH_LONG).show();
            }
        }
    }


    // Method to change icon marker on the google map
    private BitmapDescriptor bitmapDescriptorFactory(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()
                , vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void addRestaurantMarkers(List<Restaurant> restaurants, List<SelectedRestaurant> selectedRestaurants) {

        if (mMap == null) return;
// 1. Effacer tous les marqueurs
        for (Marker marker : restaurantMarkers) {
            marker.remove();
        }
        restaurantMarkers.clear();

// 2. Créer un HashMap pour vérifier rapidement si un restaurant est sélectionné
        HashMap<String, Boolean> isSelectedMap = new HashMap<>();
        if (selectedRestaurants != null && !selectedRestaurants.isEmpty()) {
            for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
                isSelectedMap.put(selectedRestaurant.getRestaurantId(), true);
            }
        }

// 3. Ajouter les marqueurs pour tous les restaurants
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



