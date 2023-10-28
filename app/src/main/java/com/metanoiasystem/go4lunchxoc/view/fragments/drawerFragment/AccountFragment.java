package com.metanoiasystem.go4lunchxoc.view.fragments.drawerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentAccountBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCurrentDateUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantForCurrentUserUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedRestaurantCallback;

public class AccountFragment extends DialogFragment {

    private FragmentAccountBinding binding;


    private  GetSelectedRestaurantForCurrentUserUseCase useCase;
    GetCurrentDateUseCase getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();

    String dateDuJour = getCurrentDateUseCase.execute();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        useCase = Injector.provideGetSelectedRestaurantForCurrentUserUseCase();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        useCase.execute(dateDuJour, new SelectedRestaurantCallback() {
            @Override
            public void onRestaurantFound(Restaurant restaurant) {
                binding.restaurantNameTextView.setText(restaurant.getRestaurantName());
                binding.restaurantAddressTextView.setText(restaurant.getRestaurantAddress());
                binding.restaurantNoNameTextView.setVisibility(View.GONE);
            }

            @Override
            public void onNoRestaurantFound() {
                binding.restaurantNoNameTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}


