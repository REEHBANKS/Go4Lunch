package com.metanoiasystem.go4lunchxoc.utils.callbacks;

public interface UseCaseCallback<ResultType> {
    void onSuccess(ResultType result);
    void onError(Throwable error);
}
