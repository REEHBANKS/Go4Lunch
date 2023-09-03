package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
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
        setupGoogleListeners();
    }

    // Set up listener for email login button
    private void setupEmailListeners() {
        binding.loginButton.setOnClickListener(view -> startEmailConnexionInActivity());
    }

    // Set up listener for Google login button
    private void setupGoogleListeners() {
        binding.gmailButton.setOnClickListener(view -> startGoogleConnexionInActivity());
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

    // Start the Google sign-in flow
    private void startGoogleConnexionInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Show a Snackbar with a message
    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {

                showSnackBar(getString(R.string.connection_succeed));
                startMainActivity();

            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }

    // Launching Main Activity
    private void startMainActivity() {
        // Create an Intent object for the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Start the MainActivity
        startActivity(intent);
    }

}