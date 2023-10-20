package com.metanoiasystem.go4lunchxoc.utils.callbacks;

public interface UseCaseFetchOneRestaurantCallback <T> {
    void onSuccess(T result);
    void onError(Throwable error);
}
