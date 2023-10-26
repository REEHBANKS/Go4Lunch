package com.metanoiasystem.go4lunchxoc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREFERENCES_NAME = "MyApp";
    private static final String KEY_RESTAURANT_NAME = "restaurantName";
    private static final String KEY_RESTAURANT_ADDRESS = "restaurantAddress";
    private static final String KEY_NOTIFICATION_STATE = "notificationState"; // Nouvelle clé pour l'état de la notification

    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Sauvegarde les informations du restaurant
    public void saveRestaurantInfo(String name, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RESTAURANT_NAME, name);
        editor.putString(KEY_RESTAURANT_ADDRESS, address);
        editor.apply();
    }

    // Récupère le nom du restaurant
    public String getRestaurantName() {
        return sharedPreferences.getString(KEY_RESTAURANT_NAME, "");
    }

    // Récupère l'adresse du restaurant
    public String getRestaurantAddress() {
        return sharedPreferences.getString(KEY_RESTAURANT_ADDRESS, "");
    }

    public boolean areNotificationsEnabled() {
        return sharedPreferences.getBoolean("notifications_enabled", true); // Par défaut, les notifications sont activées
    }


    // Sauvegarde l'état de la notification (on/off)
    public void saveNotificationState(boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_STATE, state);
        editor.apply();
    }

}

