package com.metanoiasystem.go4lunchxoc.utils;

import android.widget.RatingBar;

public class RatingUtils {

    // Sets the rating on a RatingBar with a conversion to a 3-star scale.
    public static void setRating(RatingBar ratingBar, Float rating) {
        if (rating != null) {
            // Convert the rating from a 5-star scale to a 3-star scale.
            float resultForThreeStars = 3 * rating / 5;
            ratingBar.setRating(resultForThreeStars);
        } else {
            // Set the rating to 0 if the rating is null.
            ratingBar.setRating(0);
        }
    }
}

