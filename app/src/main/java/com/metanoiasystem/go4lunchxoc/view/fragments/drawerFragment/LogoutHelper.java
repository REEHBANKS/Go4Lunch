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


    public interface LogoutListener {
        void onLogoutSuccess();
        void onLogoutFailure(String errorMessage);
    }

    private final Context context;
    private final LogoutListener listener;

    public LogoutHelper(Context context, LogoutListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void showLogoutConfirmationDialog(final Class<?> loginActivityClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.logout_title);
        builder.setMessage(R.string.logout_message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCredentials(loginActivityClass);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearCredentials(final Class<?> loginActivityClass) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            context.startActivity(new Intent(context, loginActivityClass));
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                            if (listener != null) {
                                listener.onLogoutSuccess();
                            }
                        } else {
                            if (listener != null) {
                                listener.onLogoutFailure(task.getException().getMessage());
                            }
                        }
                    }
                });
    }
}

