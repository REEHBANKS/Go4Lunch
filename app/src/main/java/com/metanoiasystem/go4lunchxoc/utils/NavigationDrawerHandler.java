package com.metanoiasystem.go4lunchxoc.utils;

import android.app.Activity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateUserViewDrawerUseCase;
import com.metanoiasystem.go4lunchxoc.view.activities.MainActivity;
import com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment.AccountFragment;
import com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment.SettingsFragment;

import java.util.Objects;

public class NavigationDrawerHandler {
    private final MainActivity mainActivity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navigationView;
    private final UpdateUserViewDrawerUseCase updateUserViewDrawerUseCase;

    public NavigationDrawerHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.drawerLayout = mainActivity.findViewById(R.id.my_drawer_layout);
        this.navigationView = mainActivity.findViewById(R.id.drawer_navigation);
        updateUserViewDrawerUseCase = Injector.provideUpdateUserViewDrawerUseCase();

    }

    public void setupNavigationDrawer() {
        // Configuration de l'icône du tiroir de navigation
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        updateUserViewDrawerUseCase.updateUserView(navigationView);

        // Configuration du NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            // Gestion des clics sur les éléments du menu (à compléter selon vos besoins)
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
                    // ...
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void openNavigationDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

}

