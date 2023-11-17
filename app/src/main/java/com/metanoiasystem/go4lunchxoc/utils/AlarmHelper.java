package com.metanoiasystem.go4lunchxoc.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;

public class AlarmHelper {

    // Context for accessing system services.
    private final Context context;

    // Constructor to initialize AlarmHelper with a context.
    public AlarmHelper(Context context) {
        this.context = context;
    }

    // Configures an alarm to trigger a notification for a restaurant.
    public void configureAlarm(String restaurantName, String restaurantAddress) {
        // Create an Intent to trigger the notification.
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("restaurantName", restaurantName);
        intent.putExtra("restaurantAddress", restaurantAddress);

        // Create a PendingIntent to be fired at alarm time.
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        // Set up the alarm to trigger the notification at 12 PM every day.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Schedule the repeating alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}


