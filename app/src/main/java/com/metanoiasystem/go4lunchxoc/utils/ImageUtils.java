package com.metanoiasystem.go4lunchxoc.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public class ImageUtils {

    // Static method to load a restaurant's image into an ImageView.
    public static void loadRestaurantImage(ImageView imageView, Restaurant restaurant) {
        // Check if the restaurant has a URL for its picture.
        if (restaurant.getUrlPictureRestaurant() != null) {

            // Use Glide to load the image from the URL.
            Glide.with(imageView.getContext())
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference="
                            + restaurant.getUrlPictureRestaurant() + "&key=" + BuildConfig.RR_KEY)
                    .into(imageView);
        } else {
            // Set a default image if the URL is not available.
            imageView.setImageResource(R.drawable.picture_restaurant_with_workers);
        }
    }
}


