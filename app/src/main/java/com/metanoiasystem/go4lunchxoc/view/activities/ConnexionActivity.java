package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityConnexionBinding;
import com.metanoiasystem.go4lunchxoc.viewmodels.WorkmatesViewModel;


import java.util.Collections;
import java.util.List;

public class ConnexionActivity extends AppCompatActivity {

    private WorkmatesViewModel userViewModel;
    private ActivityConnexionBinding binding;
    private static final int RC_SIGN_IN = 123; // Request code for sign-in process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Initialize Firebase

        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup listeners for email and Google login buttons
        setupEmailListeners();
        setupGoogleListeners();

        // Initialize the user view model
        userViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
    }

    // Sets up listener for email login button
    private void setupEmailListeners() {
        binding.loginButton.setOnClickListener(view -> startEmailConnexionInActivity());
    }

    // Sets up listener for Google login button
    private void setupGoogleListeners() {
        binding.gmailButton.setOnClickListener(view -> startGoogleConnexionInActivity());
    }

    // Starts the email sign-in flow
    private void startEmailConnexionInActivity() {
        // Specify email authentication provider
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch sign-in activity with specified theme and providers
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_login_24)
                        .build(),
                RC_SIGN_IN);
    }

    // Starts the Google sign-in flow
    private void startGoogleConnexionInActivity() {
        // Specify Google authentication provider
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Launch sign-in activity with specified theme and providers
        startActivityForResult(AuthUI.getInstance()
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
        // Handle sign-in response
        handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Displays a Snackbar with a message
    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    // Handles response after sign-in activity closes
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // Sign-in success
                userViewModel.createUser();
                showSnackBar(getString(R.string.connection_succeed));
                startMainActivity();
            } else { // Sign-in error handling
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

    // Launches the Main Activity
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
