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
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.providers.LocationProvider;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetUserChosenRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;
import com.metanoiasystem.go4lunchxoc.view.activities.RestaurantDetailActivity;
import com.metanoiasystem.go4lunchxoc.view.adapters.ListRestaurantsAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.ListRestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;
public class ListRestaurantsFragment extends Fragment implements LocationProvider.OnLocationReceivedListener, ListRestaurantsAdapter.OnRestaurantClickListener {

    private ListRestaurantsAdapter adapter;
    private final List<RestaurantWithNumberUser> restaurants = new ArrayList<>();
    private List<SelectedRestaurant> listAllSelectedRestaurants;
    private FragmentListRestaurantsBinding binding;
    private LocationProvider provider;
    private ListRestaurantsViewModel listRestaurantsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listRestaurantsViewModel = new ViewModelProvider(this).get(ListRestaurantsViewModel.class);

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



        listRestaurantsViewModel.getRestaurantWithNumberUser().observe(getViewLifecycleOwner(), this::updateUI);


        listRestaurantsViewModel.getError().observe(getViewLifecycleOwner(), throwable -> {
            Log.e("errorListViewModel", "Sorry, error!");
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        listRestaurantsViewModel.fetchRestaurantsWithSelectedUsers();



    }

    @Override
    public void onRestaurantClicked(RestaurantWithNumberUser restaurantWithNumberUser) {
        launchRestaurantDetailActivity(restaurantWithNumberUser);
    }

    private void launchRestaurantDetailActivity(RestaurantWithNumberUser restaurantWithNumberUser) {
        Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
        intent.putExtra(RestaurantDetailActivity.RESTAURANT_KEY, restaurantWithNumberUser);
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

    public void updateUI(List<RestaurantWithNumberUser> theRestaurants) {
        restaurants.clear();
        restaurants.addAll(theRestaurants);
        adapter.notifyDataSetChanged();
    }
}
