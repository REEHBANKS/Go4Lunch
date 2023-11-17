package com.metanoiasystem.go4lunchxoc.utils;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    // Static instance of MyApp for global access.
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase when the application starts.
        FirebaseApp.initializeApp(this);
        // Assign the instance for later global use.
        instance = this;
    }

    // Provides global access to the MyApp instance.
    public static MyApp getInstance() {
        return instance;
    }

    // Provides global access to the application context.
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}

