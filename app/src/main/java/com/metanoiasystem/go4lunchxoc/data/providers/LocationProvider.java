package com.metanoiasystem.go4lunchxoc.data.providers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.metanoiasystem.go4lunchxoc.view.viewholders.callbacks.LocationUpdateCallback;

public class LocationProvider {


    private final Context context;
    private final LocationUpdateCallback callback;

    public LocationProvider(Context context, LocationUpdateCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void requestLocation() {
        if (!hasLocationPermission()) {
            // Si vous n'avez pas la permission, vous pouvez lancer une exception, afficher un message ou autre chose selon votre cas d'utilisation.
            throw new SecurityException("Location permission not granted");
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            Location location = locationResult.getLocations().get(0);
                            callback.onLocationUpdated(location);
                        }
                    }
                }, Looper.getMainLooper());
    }

}