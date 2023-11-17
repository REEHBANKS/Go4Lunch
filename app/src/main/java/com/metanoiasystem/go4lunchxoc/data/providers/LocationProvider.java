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

    // Context and FusedLocationProviderClient for location services.
    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private OnLocationReceivedListener listener;
    private Location previousLocation = null;
    private static final float LOCATION_CHANGE_THRESHOLD = 100.0f;

    // Constructor initializes the FusedLocationProviderClient with the given context.
    public LocationProvider(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Requests location updates and notifies the listener when a significant location change occurs.
    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(OnLocationReceivedListener listener) {
        this.listener = listener;
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {

            // Called when the location result is received.
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    Location newLocation = locationResult.getLocations().get(latestLocationIndex);

                    if (previousLocation == null || previousLocation.distanceTo(newLocation) > LOCATION_CHANGE_THRESHOLD) {
                        listener.onLocationReceived(newLocation.getLatitude(), newLocation.getLongitude());
                        previousLocation = newLocation;
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    // Requests the current location and notifies the listener.
    public void requestCurrentLocation(OnLocationReceivedListener listener) {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                listener.onLocationReceived(location.getLatitude(), location.getLongitude());
            }
        });
    }

    // Stops requesting location updates.
    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    // Interface for receiving location updates.
    public interface OnLocationReceivedListener {
        void onLocationReceived(double latitude, double longitude);
    }
}
