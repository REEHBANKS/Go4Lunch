package com.metanoiasystem.go4lunchxoc.utils;

import android.content.Context;
import android.text.Html;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public class RestaurantStatusUtils {

    public static CharSequence getOpeningStatus(Context context, Restaurant restaurant) {
        // Si l'information n'est pas disponible
        if (restaurant.getOpeningHours() == null) {
            return context.getString(R.string.not_available);
        }

        int resId = restaurant.getOpeningHours() ? R.string.open : R.string.closed;
        return Html.fromHtml(context.getString(resId));
    }

}
