package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.providers.LocationProvider;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CountUsersForRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.view.activities.RestaurantDetailActivity;
import com.metanoiasystem.go4lunchxoc.view.adapters.ListRestaurantsAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory.RestaurantViewModelFactory;

import java.util.ArrayList;
import java.util.List;
public class ListRestaurantsFragment extends Fragment implements LocationProvider.OnLocationReceivedListener, ListRestaurantsAdapter.OnRestaurantClickListener {

    private ListRestaurantsAdapter adapter;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<SelectedRestaurant> listAllSelectedRestaurants;
    private FragmentListRestaurantsBinding binding;
    private LocationProvider provider;
    private ListRestaurantsViewModel listRestaurantsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FetchRestaurantListUseCase fetchRestaurantListUseCase = Injector.provideFetchRestaurantListUseCase();
        GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase = Injector.provideGetAllSelectedRestaurantsUseCase();
        GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase = Injector.provideGetAllRestaurantsFromFirebaseUseCase();
        RestaurantViewModelFactory factory = new RestaurantViewModelFactory(fetchRestaurantListUseCase,
                 getAllSelectedRestaurantsUseCase,getAllRestaurantsFromFirebaseUseCase);
        listRestaurantsViewModel = new ViewModelProvider(this, factory).get(ListRestaurantsViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListRestaurantsBinding.inflate(getLayoutInflater(), container, false);

        provider = new LocationProvider(requireContext());
        configureRecyclerView();
        listRestaurantsViewModel.setGetAllSelectedRestaurantsUseCase();

        provider.requestLocationUpdates(this);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();



        listRestaurantsViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::updateUI);
        listRestaurantsViewModel.getSelectedRestaurants().observe(getViewLifecycleOwner(), selectedRestaurants -> {
            adapter.setAllSelectedRestaurants(selectedRestaurants);
            listAllSelectedRestaurants = selectedRestaurants;
        });

        listRestaurantsViewModel.getCountUsersPerRestaurantLiveData().observe(getViewLifecycleOwner(), countMap -> {
            adapter.setCountUsersMap(countMap);
            adapter.notifyDataSetChanged();
        });

        listRestaurantsViewModel.getError().observe(getViewLifecycleOwner(), throwable -> {
            Log.e("errorListViewModel", "Sorry, error!");
        });
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
       // listRestaurantsViewModel.fetchRestaurants(latitude, longitude);
    }

    private void configureRecyclerView() {
        this.adapter = new ListRestaurantsAdapter(this.restaurants, this);
        binding.fragmentMainRecyclerView.setAdapter(this.adapter);
        this.binding.fragmentMainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateUI(List<Restaurant> theRestaurants) {
        restaurants.clear();
        restaurants.addAll(theRestaurants);
        adapter.notifyDataSetChanged();
    }
}
