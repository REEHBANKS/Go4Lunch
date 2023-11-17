package com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.metanoiasystem.go4lunchxoc.databinding.FragmentAccountBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentSettingsBinding;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.PreferencesManager;

import java.util.Objects;

public class SettingsFragment extends DialogFragment {

    private FragmentSettingsBinding binding;

    // Inflate the layout for this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    // Setup the fragment's view components
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set a click listener for the close button
        binding.closeWindowsSettingFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog fragment
            }
        });

        // Set a listener for the alarm switch
        binding.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Log the switch state change
                Log.d("GRAYCE", "BTN CHECK ON");
                // Create an instance of PreferencesManager
                PreferencesManager preferencesManager = new PreferencesManager(requireContext());
                // Save the new notification state
                preferencesManager.saveNotificationState(isChecked);
            }
        });
    }
}

