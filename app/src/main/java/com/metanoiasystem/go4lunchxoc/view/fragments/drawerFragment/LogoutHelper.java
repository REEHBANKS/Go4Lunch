package com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.metanoiasystem.go4lunchxoc.R;

public class LogoutHelper {

    // Interface for handling logout callbacks
    public interface LogoutListener {
        void onLogoutSuccess();
        void onLogoutFailure(String errorMessage);
    }

    private final Context context;
    private final LogoutListener listener;

    // Constructor initializing context and listener
    public LogoutHelper(Context context, LogoutListener listener) {
        this.context = context;
        this.listener = listener;
    }

    // Display a confirmation dialog for logging out
    public void showLogoutConfirmationDialog(final Class<?> loginActivityClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.logout_title); // Set title for the dialog
        builder.setMessage(R.string.logout_message); // Set message for the dialog

        // Positive button for confirmation
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCredentials(loginActivityClass); // Clear credentials and logout
            }
        });

        // Negative button for cancellation
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        AlertDialog dialog = builder.create(); // Create the dialog
        dialog.show(); // Show the dialog
    }

    // Method to clear user credentials and sign out
    private void clearCredentials(final Class<?> loginActivityClass) {
        AuthUI.getInstance()
                .signOut(context) // Sign out from Firebase Authentication
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut(); // Additional sign out for Firebase Auth
                            context.startActivity(new Intent(context, loginActivityClass)); // Redirect to login activity
                            if (context instanceof Activity) {
                                ((Activity) context).finish(); // Finish the current activity
                            }
                            if (listener != null) {
                                listener.onLogoutSuccess(); // Notify logout success
                            }
                        } else {
                            if (listener != null) {
                                listener.onLogoutFailure(task.getException().getMessage()); // Notify logout failure
                            }
                        }
                    }
                });
    }
}

