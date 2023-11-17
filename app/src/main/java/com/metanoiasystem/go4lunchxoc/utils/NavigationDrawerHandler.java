package com.metanoiasystem.go4lunchxoc.utils;

import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateUserViewDrawerUseCase;
import com.metanoiasystem.go4lunchxoc.view.activities.ConnexionActivity;
import com.metanoiasystem.go4lunchxoc.view.activities.MainActivity;
import com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment.AccountFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment.LogoutHelper;
import com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment.SettingsFragment;

import java.util.Objects;

public class NavigationDrawerHandler {
    // References to main activity components and use case for updating the user view.
    private final MainActivity mainActivity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navigationView;
    private final UpdateUserViewDrawerUseCase updateUserViewDrawerUseCase;

    // Constructor initializing with the main activity context.
    public NavigationDrawerHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.drawerLayout = mainActivity.findViewById(R.id.my_drawer_layout);
        this.navigationView = mainActivity.findViewById(R.id.drawer_navigation);
        updateUserViewDrawerUseCase = Injector.provideUpdateUserViewDrawerUseCase();
    }

    // Sets up the navigation drawer with menu items, actions, and user view.
    public void setupNavigationDrawer() {
        // Configure the navigation drawer icon and update the user view.
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        updateUserViewDrawerUseCase.updateUserView(navigationView);

        // Set up the NavigationView with item selection listeners.
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle menu item clicks with switch-case (extend as per your requirements).
            switch (item.getItemId()) {
                case R.id.nav_account:
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new AccountFragment())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.nav_settings:
                    DialogFragment settingsDialog = new SettingsFragment();
                    settingsDialog.show(mainActivity.getSupportFragmentManager(), "SettingsDialog");
                    break;
                case R.id.nav_logout:
                    LogoutHelper logoutHelper = new LogoutHelper(mainActivity, new LogoutHelper.LogoutListener() {
                        @Override
                        public void onLogoutSuccess() {
                            Toast.makeText(mainActivity.getApplicationContext(), R.string.logout_text, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLogoutFailure(String errorMessage) {
                            Toast.makeText(mainActivity.getApplicationContext(), R.string.error_logout, Toast.LENGTH_SHORT).show();
                        }
                    });
                    logoutHelper.showLogoutConfirmationDialog(ConnexionActivity.class);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    // Opens the navigation drawer.
    public void openNavigationDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

}

