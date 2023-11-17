package com.metanoiasystem.go4lunchxoc.utils;

import android.content.Context;
import android.text.Html;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public class RestaurantStatusUtils {

    // Returns the opening status of a restaurant as a CharSequence.
    public static CharSequence getOpeningStatus(Context context, Restaurant restaurant) {
        // Check if the opening hours information is available.
        if (restaurant.getOpeningHours() == null) {
            // Return "Not Available" if the information is missing.
            return context.getString(R.string.not_available);
        }

        // Determine the resource ID based on whether the restaurant is open or closed.
        int resId = restaurant.getOpeningHours() ? R.string.open : R.string.closed;
        // Return the opening status formatted as HTML.
        return Html.fromHtml(context.getString(resId));
    }
}

