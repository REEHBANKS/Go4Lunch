package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesBinding;
import com.metanoiasystem.go4lunchxoc.view.adapters.WorkmatesAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private WorkmatesAdapter adapter;
    private WorkmatesViewModel userViewModel;
    private List<UserAndPictureWithYourSelectedRestaurant> users;

    // Initialize the fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel for managing user data
        userViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
    }

    // Inflate the layout for this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        // Setup the RecyclerView to display users
        setupRecyclerView();

        // Observe changes in the ViewModel data
        observeViewModel();

        // Fetch users and their chosen restaurants
        userViewModel.fetchUserChosenRestaurants();

        return binding.getRoot();
    }

    // Handle fragment resuming
    @Override
    public void onResume() {
        super.onResume();
        // Fetch users again when the fragment is active
        userViewModel.fetchUserChosenRestaurants();
    }

    // Setup the RecyclerView with the adapter and layout manager
    private void setupRecyclerView() {
        this.users = new ArrayList<>();
        this.adapter = new WorkmatesAdapter(this.users);
        binding.fragmentListWorkmates.setAdapter(this.adapter);
        this.binding.fragmentListWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // Update the UI with a new list of users
    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<UserAndPictureWithYourSelectedRestaurant> theUsers){
        users.clear();
        users.addAll(theUsers);
        // Notify the adapter of data changes to update the RecyclerView
        adapter.notifyDataSetChanged();
    }

    // Observe data changes in the ViewModel
    private void observeViewModel() {
        // Observe user data changes
        userViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateUI);

        // Observe and handle error messages
        userViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            // Display error messages to the user
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });
    }
}

