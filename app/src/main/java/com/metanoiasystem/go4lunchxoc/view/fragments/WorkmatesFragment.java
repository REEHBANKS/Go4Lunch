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

import com.metanoiasystem.go4lunchxoc.data.models.User;
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

   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        // Configuration du RecyclerView
        setupRecyclerView();

        // Observer les données du ViewModel
        observeViewModel();

        // Récupération des utilisateurs
        userViewModel.fetchUserChosenRestaurants();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Récupération des utilisateurs à chaque fois que le fragment redevient actif
        userViewModel.fetchUserChosenRestaurants();
    }

    private void setupRecyclerView() {
        this.users = new ArrayList<>();
        this.adapter = new WorkmatesAdapter(this.users);
        binding.fragmentListWorkmates.setAdapter(this.adapter);
        this.binding.fragmentListWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<UserAndPictureWithYourSelectedRestaurant> theUsers){
        users.clear();
        users.addAll(theUsers);
        adapter.notifyDataSetChanged();
    }

    private void observeViewModel() {
        userViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateUI);

        userViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            // Show the error message to the user
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

    }

}
