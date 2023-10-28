package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.utils.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        restaurantSelectorListViewModel = new ViewModelProvider(this).get(RestaurantSelectorListViewModel.class);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantSelectorListBinding.inflate(getLayoutInflater(), container, false);
        this.configureRecyclerView();


        if (getArguments() != null) {
            RestaurantWithNumberUser restaurantWithNumberUser = (RestaurantWithNumberUser) getArguments().getSerializable(RESTAURANT_KEY);
            // Passer l'ID du restaurant au ViewModel

            restaurantSelectorListViewModel.getUsersWhoSelectedThisRestaurant(restaurantWithNumberUser.getRestaurant().getId());

            observeGetUsersDetailScreen();


        }

        return binding.getRoot();
    }

    public void observeGetUsersDetailScreen(){
        restaurantSelectorListViewModel.getUsersDetailScreen().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> usersObserve) {
                updateUI(usersObserve);
            }
        });

    }

    public void updateUI(List<User> usersListDetailScreen) {
        users.addAll(usersListDetailScreen);
        adapter.notifyDataSetChanged();
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