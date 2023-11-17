package com.metanoiasystem.go4lunchxoc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    // Constants for SharedPreferences.
    private static final String PREFERENCES_NAME = "MyApp";
    private static final String KEY_RESTAURANT_NAME = "restaurantName";
    private static final String KEY_RESTAURANT_ADDRESS = "restaurantAddress";
    private static final String KEY_NOTIFICATION_STATE = "notificationState"; // Key for notification state

    // SharedPreferences instance.
    private final SharedPreferences sharedPreferences;

    // Constructor initializing with the application context.
    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Saves restaurant information (name and address) in SharedPreferences.
    public void saveRestaurantInfo(String name, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RESTAURANT_NAME, name);
        editor.putString(KEY_RESTAURANT_ADDRESS, address);
        editor.apply();
    }

    // Retrieves the saved restaurant name.
    public String getRestaurantName() {
        return sharedPreferences.getString(KEY_RESTAURANT_NAME, "");
    }

    // Retrieves the saved restaurant address.
    public String getRestaurantAddress() {
        return sharedPreferences.getString(KEY_RESTAURANT_ADDRESS, "");
    }

    // Checks if notifications are enabled.
    public boolean areNotificationsEnabled() {
        return sharedPreferences.getBoolean("notifications_enabled", true); // Default is enabled.
    }

    // Saves the notification state (enabled/disabled).
    public void saveNotificationState(boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_STATE, state);
        editor.apply();
    }
}

