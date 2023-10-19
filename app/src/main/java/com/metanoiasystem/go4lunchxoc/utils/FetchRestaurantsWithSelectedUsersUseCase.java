package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import java.util.List;

public interface FetchRestaurantsWithSelectedUsersUseCase {
    void execute(UseCaseCallback<List<RestaurantWithNumberUser>> callback);
}
