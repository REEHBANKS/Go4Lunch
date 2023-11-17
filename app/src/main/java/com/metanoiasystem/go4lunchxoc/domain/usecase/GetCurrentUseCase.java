package com.metanoiasystem.go4lunchxoc.domain.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetCurrentUseCase {
    public FirebaseUser execute() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}

