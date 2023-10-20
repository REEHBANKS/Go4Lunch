package com.metanoiasystem.go4lunchxoc.utils.callbacks;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;

import java.util.List;

public interface RepositoryFetchAllRestaurantFetchCallback {
    void onSuccess(List<Restaurant> restaurants);
    void onError(Throwable error);
}
