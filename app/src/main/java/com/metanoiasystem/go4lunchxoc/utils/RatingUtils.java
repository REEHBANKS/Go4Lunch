package com.metanoiasystem.go4lunchxoc.utils;

import android.widget.RatingBar;

public class RatingUtils {

    public static void setRating(RatingBar ratingBar, Float rating) {
        if (rating != null) {
            float resultForThreeStars = 3 * rating / 5;
            ratingBar.setRating(resultForThreeStars);
        } else {

            ratingBar.setRating(0);
        }
    }
}
