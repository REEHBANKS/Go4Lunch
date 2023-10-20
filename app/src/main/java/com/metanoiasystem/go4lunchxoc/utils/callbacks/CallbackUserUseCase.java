package com.metanoiasystem.go4lunchxoc.utils.callbacks;

import com.metanoiasystem.go4lunchxoc.data.models.User;

public interface CallbackUserUseCase<User> {
    void onUserDataFetched(User user);
    void onError(Exception e);
}
