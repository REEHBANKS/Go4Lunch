package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListBinding;
import com.metanoiasystem.go4lunchxoc.view.adapters.RestaurantSelectorListAdapter;

import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantSelectorListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSelectorListFragment extends Fragment {

    FragmentRestaurantSelectorListBinding binding;
    private List<User> users;
    private RestaurantSelectorListAdapter adapter;
    RestaurantSelectorListViewModel restaurantSelectorListViewModel;
    public static String RESTAURANT_KEY = "RESTAURANT_KEY";

    // Initialize the fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        restaurantSelectorListViewModel = new ViewModelProvider(this).get(RestaurantSelectorListViewModel.class);
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRestaurantSelectorListBinding.inflate(getLayoutInflater(), container, false);
        // Configure the RecyclerView
        this.configureRecyclerView();

        // Check if the fragment has arguments and process them
        if (getArguments() != null) {
            RestaurantWithNumberUser restaurantWithNumberUser = (RestaurantWithNumberUser) getArguments().getSerializable(RESTAURANT_KEY);
            // Fetch users who selected the given restaurant
            restaurantSelectorListViewModel.getUsersWhoSelectedThisRestaurant(restaurantWithNumberUser.getRestaurant().getId());
            // Observe changes in the list of users
            observeGetUsersDetailScreen();
        }

        return binding.getRoot();
    }

    // Observe ViewModel for changes in the user list
    public void observeGetUsersDetailScreen(){
        restaurantSelectorListViewModel.getUsersDetailScreen().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> usersObserve) {
                // Update the UI when the list of users changes
                updateUI(usersObserve);
            }
        });
    }

    // Update the UI with the latest list of users
    public void updateUI(List<User> usersListDetailScreen) {
        users.addAll(usersListDetailScreen);
        // Notify the adapter of the data change
        adapter.notifyDataSetChanged();
    }

    // Configure the RecyclerView
    private void configureRecyclerView() {
        this.users = new ArrayList<>();
        this.adapter = new RestaurantSelectorListAdapter(this.users);
        // Set the adapter and layout manager for the RecyclerView
        binding.fragmentRestaurantSelectorListRecyclerView.setAdapter(this.adapter);
        this.binding.fragmentRestaurantSelectorListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
