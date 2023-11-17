package com.metanoiasystem.go4lunchxoc.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.metanoiasystem.go4lunchxoc.R;

public class NotificationReceiver extends BroadcastReceiver {

    // Constants for the notification channel and notification ID.
    private static final String CHANNEL_ID = "restaurant_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract restaurant details from the intent.
        String restaurantName = intent.getStringExtra("restaurantName");
        String restaurantAddress = intent.getStringExtra("restaurantAddress");

        // Get the NotificationManager service.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for API 26+ (Android Oreo).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Restaurant Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification with details about the selected restaurant.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Restaurant Reminder")
                .setContentText("You have chosen: " + restaurantName + " at address: " + restaurantAddress)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Notify the user with the built notification.
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

