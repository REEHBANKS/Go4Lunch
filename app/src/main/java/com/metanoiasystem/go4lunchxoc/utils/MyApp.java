package com.metanoiasystem.go4lunchxoc.utils;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    private static MyApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        instance = this;
    }


    public static MyApp getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
