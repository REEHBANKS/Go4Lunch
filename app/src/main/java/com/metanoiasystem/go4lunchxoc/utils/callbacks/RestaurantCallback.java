package com.metanoiasystem.go4lunchxoc.utils.callbacks;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

public interface RestaurantCallback {
    void onRestaurantReceived(Restaurant restaurant);
    void onError(Throwable throwable);
}
