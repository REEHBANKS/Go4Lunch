package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.metanoiasystem.go4lunchxoc.domain.usecase.CountUsersForRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory.RestaurantViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MapFragment extends Fragment implements LocationProvider.OnLocationReceivedListener {

    private LocationProvider locationProvider;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private GoogleMap mMap;
    private  MapViewModel mapViewModel;
    private Marker userMarker;
    private  List<Restaurant> allRestaurants = new ArrayList<>();
    private  List<SelectedRestaurant> selectedAllRestaurants = new ArrayList<>();
    private final List<Marker> restaurantMarkers = new ArrayList<>();
    private boolean locationRequested = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FetchRestaurantListUseCase fetchRestaurantListUseCase = Injector.provideFetchRestaurantListUseCase();
        CountUsersForRestaurantUseCase countUsersForRestaurantUseCase = Injector.provideCountUsersForRestaurantUseCase();
        GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase = Injector.provideGetAllSelectedRestaurantsUseCase();

        RestaurantViewModelFactory factory = new RestaurantViewModelFactory(fetchRestaurantListUseCase,
                countUsersForRestaurantUseCase, getAllSelectedRestaurantsUseCase);
        mapViewModel = new ViewModelProvider(this, factory).get(MapViewModel.class);
        mapViewModel.fetchAllSelectedRestaurants();

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

        mapViewModel.getMapLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            allRestaurants = restaurants;
            addRestaurantMarkers(allRestaurants,selectedAllRestaurants);
        });

        mapViewModel.getSelectedRestaurants().observe(getViewLifecycleOwner(), selectedRestaurants ->{

            selectedAllRestaurants = selectedRestaurants;
            addRestaurantMarkers(allRestaurants,selectedAllRestaurants);
        } );


    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(this);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        locationProvider.stopLocationUpdates();
        userMarker = null;
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            checkAccessRestaurant();
        }
    };

    private void checkAccessRestaurant() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (!locationRequested) {
                locationProvider.requestCurrentLocation(new LocationProvider.OnLocationReceivedListener() {
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        // Votre code pour traiter la nouvelle localisation reçue
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

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        mapViewModel.fetchRestaurants(latitude, longitude);
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

        // Créer un HashMap pour vérifier rapidement si un restaurant est sélectionné
        HashMap<String, Boolean> isSelectedMap = new HashMap<>();
        for (SelectedRestaurant selectedRestaurant : selectedRestaurants) {
            isSelectedMap.put(selectedRestaurant.getRestaurantId(), true);
        }

        // Créer un set de tous les restaurants par leur id
        Set<String> newRestaurants = new HashSet<>();
        for (Restaurant restaurant : restaurants) {
            newRestaurants.add(restaurant.getId());
        }

        // Supprimer les marqueurs des restaurants qui ne sont plus dans la liste
        Iterator<Marker> iterator = restaurantMarkers.iterator();
        while (iterator.hasNext()) {
            Marker marker = iterator.next();
            Restaurant restaurant = (Restaurant) marker.getTag();
            if (!newRestaurants.contains(restaurant.getId())) {
                marker.remove();
                iterator.remove();
            }
        }

        // Ajouter les marqueurs pour les nouveaux restaurants
        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
            // Vérifiez si le marqueur pour ce restaurant existe déjà
            boolean exists = false;
            for (Marker marker : restaurantMarkers) {
                if (marker.getTag().equals(restaurant)) {
                    exists = true;
                    break;
                }
            }
            // Si le marqueur n'existe pas, créez-en un nouveau
            if (!exists) {
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




}



