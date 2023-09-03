package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.util.Logger;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityConnexionBinding;


import java.util.Collections;
import java.util.List;

public class ConnexionActivity extends AppCompatActivity {

    private ActivityConnexionBinding binding;
    private static final int RC_SIGN_IN = 123; // Request code for sign in


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //FirebaseAuth.getInstance().setLogLevel(Logger.Level.DEBUG);


        setupEmailListeners();
    }

    // Set up listener for email login button
    private void setupEmailListeners() {
        binding.loginButton.setOnClickListener(view -> startEmailConnexionInActivity());
    }

    // Start the email sign-in flow
    private void startEmailConnexionInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity with specified theme, providers, and logo
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_login_24)
                        .build(),
                RC_SIGN_IN);
    }


}