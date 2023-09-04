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

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.view.adapters.ListRestaurantsAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListRestaurantsFragment extends Fragment {

    private ListRestaurantsAdapter adapter;
    private List<Restaurant> restaurants;
    private FragmentListRestaurantsBinding binding;


    private ListRestaurantsViewModel listRestaurantsViewModel;
    double mapLatitude = 49.1479317;
    double mapLongitude = 2.2466113;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListRestaurantsBinding.inflate(getLayoutInflater(), container, false);
        this.configureRecyclerView();

        listRestaurantsViewModel = new ViewModelProvider(this).get(ListRestaurantsViewModel.class);

        listRestaurantsViewModel.getListRestaurants().observe(getViewLifecycleOwner(), this::updateUI);


        listRestaurantsViewModel.fetchListRestaurants(mapLatitude, mapLongitude);


        return binding.getRoot();
    }

    // -----------------
    // CONFIGURATION RECYCLERVIEW
    // -----------------
    private void configureRecyclerView() {
        this.restaurants = new ArrayList<>();
        this.adapter = new ListRestaurantsAdapter(this.restaurants);
        binding.fragmentMainRecyclerView.setAdapter(this.adapter);
        this.binding.fragmentMainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // -------------------
    // UPDATE UI
    // -------------------
    @SuppressLint("NotifyDataSetChanged")
    public void updateUI(List<Restaurant> theRestaurants) {
        restaurants.addAll(theRestaurants);
        adapter.notifyDataSetChanged();
    }

}