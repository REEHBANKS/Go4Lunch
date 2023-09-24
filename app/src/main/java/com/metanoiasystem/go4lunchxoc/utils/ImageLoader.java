package com.metanoiasystem.go4lunchxoc.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.R;

public class ImageLoader {

    private static final String DEFAULT_PICTURE = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference=";

    public static void loadRestaurantImage(ImageView imageView, String pictureReference) {
        if (pictureReference != null) {
            String fullPath = DEFAULT_PICTURE + pictureReference + "&key=" + BuildConfig.RR_KEY;
            Glide.with(imageView.getContext())
                    .load(fullPath)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.picture_restaurant_with_workers);
        }
    }
}
