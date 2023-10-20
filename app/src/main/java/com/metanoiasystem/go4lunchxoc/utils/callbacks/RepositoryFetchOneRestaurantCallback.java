package com.metanoiasystem.go4lunchxoc.utils.callbacks;

public interface RepositoryFetchOneRestaurantCallback<T> {
    void onSuccess(T result);
    void onError(Throwable error);
}

