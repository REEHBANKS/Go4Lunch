package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.providers.LocationProvider;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.view.activities.RestaurantDetailActivity;
import com.metanoiasystem.go4lunchxoc.view.adapters.ListRestaurantsAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;
public class ListRestaurantsFragment extends Fragment implements LocationProvider.OnLocationReceivedListener, ListRestaurantsAdapter.OnRestaurantClickListener {

    private ListRestaurantsAdapter adapter;
    private List<Restaurant> restaurants;
    private FragmentListRestaurantsBinding binding;
    private LocationProvider provider;
    private ListRestaurantsViewModel listRestaurantsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        provider = new LocationProvider(requireContext());

        binding = FragmentListRestaurantsBinding.inflate(getLayoutInflater(), container, false);
        this.configureRecyclerView();

        listRestaurantsViewModel = new ViewModelProvider(this).get(ListRestaurantsViewModel.class);
        listRestaurantsViewModel.getListRestaurants().observe(getViewLifecycleOwner(), this::updateUI);

        provider.requestLocationUpdates(this);
        return binding.getRoot();
    }

    @Override
    public void onRestaurantClicked(Restaurant restaurant) {
        launchRestaurantDetailActivity(restaurant);
    }

    private void launchRestaurantDetailActivity(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
        intent.putExtra(RestaurantDetailActivity.RESTAURANT_KEY, restaurant);
        startActivity(intent);
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        listRestaurantsViewModel.fetchListRestaurants(latitude, longitude);
    }

    // -----------------
    // CONFIGURATION RECYCLERVIEW
    // -----------------
    private void configureRecyclerView() {
        this.restaurants = new ArrayList<>();
        this.adapter = new ListRestaurantsAdapter(this.restaurants, this);
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