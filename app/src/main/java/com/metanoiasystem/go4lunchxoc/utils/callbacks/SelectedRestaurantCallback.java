package com.metanoiasystem.go4lunchxoc.utils.callbacks;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;

public interface SelectedRestaurantCallback {

    void onRestaurantFound(Restaurant restaurant);
    void onNoRestaurantFound();
    void onError(Throwable e);
}
