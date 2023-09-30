package com.metanoiasystem.go4lunchxoc.utils;

import android.content.Context;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public class RestaurantStatusUtils {

    public static String getOpeningStatus(Context context, Restaurant restaurant) {
        int resId = restaurant.getOpeningHours() ? R.string.openn : R.string.closed;
        return context.getString(resId);
    }

}
