package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentUseCase;

public class GetCurrentUseCaseImpl implements GetCurrentUseCase {
    @Override
    public FirebaseUser execute() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}

