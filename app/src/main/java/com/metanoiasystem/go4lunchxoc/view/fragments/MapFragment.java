package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.metanoiasystem.go4lunchxoc.data.providers.LocationProvider;
import com.metanoiasystem.go4lunchxoc.view.callbacks.LocationUpdateCallback;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements LocationUpdateCallback {


    private LocationProvider locationProvider;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private GoogleMap mMap;
    private final List<Restaurant> mapRestaurants = new ArrayList<>();
    MapViewModel mapViewModel = new MapViewModel();
    private Marker userMarker;
    private final List<Marker> restaurantMarkers = new ArrayList<>();






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationProvider = new LocationProvider(requireContext(), this);

        mapViewModel.getMapLiveData().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                addRestaurantMarkers(restaurants);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        locationProvider.requestLocation();
    }

   @Override
   public void onPause() {
        super.onPause();
        userMarker = null;

   }



    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            checkAccessRestaurant();


        }
    };

    private void checkAccessRestaurant() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            //
            locationProvider.requestLocation();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // La permission est accordée, activez la localisation
                locationProvider.requestLocation();
            } else {
                // Montrez un message à l'utilisateur expliquant pourquoi vous avez besoin de cette permission
                Toast.makeText(getContext(), "Permission required to show location on map", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onLocationUpdated(Location location) {
        mapViewModel.fetchMapViewModel(location.getLatitude(), location.getLongitude());
        setMarkerAtCurrentPosition(location);
    }

    private void setMarkerAtCurrentPosition(Location location) {
        if (mMap == null || location == null) return;

        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if(userMarker == null) {
            userMarker = mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFactory(getContext(), R.drawable.icon_you_are_here))
                    .position(myLocation)
                    .title("You're here!"));
            mMap.getUiSettings().setZoomControlsEnabled(true);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));


        } else {
            userMarker.setPosition(myLocation);
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

    private void addRestaurantMarkers(List<Restaurant> restaurants) {
        if (mMap == null ) return;

        // Supprimer les marqueurs des restaurants précédents
        for (Marker marker : restaurantMarkers) {
            marker.remove();
        }
        restaurantMarkers.clear();

        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFactory(getContext(), R.drawable.icon_green_lunch))
                    .position(restaurantLocation)
                    .title(restaurant.getRestaurantName()));
        }
    }


}



