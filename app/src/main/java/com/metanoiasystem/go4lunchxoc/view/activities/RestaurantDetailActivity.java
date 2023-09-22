package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityRestaurantDetailBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantDetailViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory.RestaurantDetailViewModelFactory;

public class RestaurantDetailActivity extends AppCompatActivity {

    public static String RESTAURANT_KEY = "RESTAURANT_KEY";
    private Restaurant restaurant;
    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AddToFavoritesUseCase addToFavoritesUseCase = Injector.provideAddToFavoritesUseCase();
        RestaurantDetailViewModelFactory restaurantDetailViewModelFactory = new RestaurantDetailViewModelFactory(addToFavoritesUseCase);
        viewModel = new ViewModelProvider(this, restaurantDetailViewModelFactory).get(RestaurantDetailViewModel.class);


        // Récupération de l'objet Restaurant passé en extra
        restaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT_KEY);

        observeViewModel();
        updateUi();
    }


    public void updateUi() {
        // Mettez à jour l'UI en fonction de l'objet 'restaurant'

        binding.nameRestaurantDetail.setText(restaurant.getRestaurantName());

        binding.buttonLikeRestaurantDetail.setOnClickListener(view -> {
            if (restaurant != null) {
                viewModel.addRestaurantToFavorites(restaurant.getId());
            }
        });

    }

    private void observeViewModel() {
        viewModel.getAddSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Restaurant ajouté aux favoris avec succès!", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.errorMessage.observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

}