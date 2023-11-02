package com.metanoiasystem.go4lunchxoc.data.providers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.Task;

public class LocationProvider {

    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private OnLocationReceivedListener listener;
    private Location previousLocation = null;
    private static final float LOCATION_CHANGE_THRESHOLD = 100.0f;

    public LocationProvider(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(OnLocationReceivedListener listener) {
        this.listener = listener;
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {


            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    Location newLocation = locationResult.getLocations().get(latestLocationIndex);

                    if (previousLocation == null || previousLocation.distanceTo(newLocation) > LOCATION_CHANGE_THRESHOLD) {
                        double latitude = newLocation.getLatitude();
                        double longitude = newLocation.getLongitude();
                        listener.onLocationReceived(latitude, longitude);
                        previousLocation = newLocation;
                    }
                }
            }


        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void requestCurrentLocation(OnLocationReceivedListener listener) {
        // Ici, vous pourriez soit envoyer la dernière localisation connue, soit faire une seule requête de localisation
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                listener.onLocationReceived(location.getLatitude(), location.getLongitude());
            }
        });
    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    public interface OnLocationReceivedListener {
        void onLocationReceived(double latitude, double longitude);
    }
}
