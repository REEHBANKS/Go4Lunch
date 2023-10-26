package com.metanoiasystem.go4lunchxoc.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class AlarmHelper {

    private Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }


    public void configureAlarm(String restaurantName, String restaurantAddress) {
        Log.d("GRAYCE", "ALARM START ACTIVED");
        // Créez une intention pour déclencher la notification
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("restaurantName", restaurantName);
        intent.putExtra("restaurantAddress", restaurantAddress);

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        // Configurez l'alarme pour déclencher la notification à 12h
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 54);
        calendar.set(Calendar.SECOND, 0);
        Log.d("GRAYCE", "ALARM END ACTIVED");

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}

