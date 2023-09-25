package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListBinding;
import com.metanoiasystem.go4lunchxoc.view.adapters.RestaurantSelectorListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSelectorListFragment extends Fragment {

    FragmentRestaurantSelectorListBinding binding;
    private List<User> users;
    private RestaurantSelectorListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantSelectorListBinding.inflate(getLayoutInflater(), container, false);
        this.configureRecyclerView();

        return binding.getRoot();
    }

    // -----------------
    // CONFIGURATION RECYCLERVIEW
    // -----------------
    private void configureRecyclerView() {
        this.users = new ArrayList<>();
        this.adapter = new RestaurantSelectorListAdapter(this.users);
        binding.fragmentRestaurantSelectorListRecyclerView.setAdapter(this.adapter);
        this.binding.fragmentRestaurantSelectorListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}