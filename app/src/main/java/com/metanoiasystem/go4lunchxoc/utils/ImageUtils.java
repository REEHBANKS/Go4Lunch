package com.metanoiasystem.go4lunchxoc.utils;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public class ImageUtils {

    public static void loadRestaurantImage(ImageView imageView, Restaurant restaurant) {
        if (restaurant.getUrlPictureRestaurant() != null) {
            Log.d("RESTAURANT_IMAGE_LOAD", "Loading image for restaurant: " + restaurant.getRestaurantName());
            Glide.with(imageView.getContext())
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference="
                            + restaurant.getUrlPictureRestaurant() + "&key=" + BuildConfig.RR_KEY)
                    .into(imageView);


        } else {
            imageView.setImageResource(R.drawable.picture_restaurant_with_workers);
        }
    }
}

