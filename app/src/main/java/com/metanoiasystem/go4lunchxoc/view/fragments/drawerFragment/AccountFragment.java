package com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentAccountBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCurrentDateUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantForCurrentUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedRestaurantCallback;

public class AccountFragment extends DialogFragment {

    private FragmentAccountBinding binding;

    private GetSelectedRestaurantForCurrentUserUseCase useCase;
    GetCurrentDateUseCase getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    // Get the current date
    String dateDuJour = getCurrentDateUseCase.execute();

    // Initialize the fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the UseCase for getting the selected restaurant for the current user
        useCase = Injector.provideGetSelectedRestaurantForCurrentUserUseCase();
    }

    // Inflate the layout for this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    // Handle view creation
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Execute the use case to find the selected restaurant for the current user
        useCase.execute(dateDuJour, new SelectedRestaurantCallback() {
            // Handle the case when a restaurant is found
            @Override
            public void onRestaurantFound(Restaurant restaurant) {
                // Set the restaurant name and address in the UI
                binding.restaurantNameTextView.setText(restaurant.getRestaurantName());
                binding.restaurantAddressTextView.setText(restaurant.getRestaurantAddress());
                // Hide the 'no restaurant selected' text view
                binding.restaurantNoNameTextView.setVisibility(View.GONE);
            }

            // Handle the case when no restaurant is found
            @Override
            public void onNoRestaurantFound() {
                // Show the 'no restaurant selected' text view
                binding.restaurantNoNameTextView.setVisibility(View.VISIBLE);
            }

            // Handle errors
            @Override
            public void onError(Throwable e) {
                // Error handling can be implemented here
            }
        });
    }
}



