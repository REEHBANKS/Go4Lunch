package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityRestaurantDetailBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.utils.ImageLoader;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
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

        // Récupération des UseCases depuis l'Injector
        AddToFavoritesUseCase addToFavoritesUseCase = Injector.provideAddToFavoritesUseCase();
        CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase = Injector.provideCheckIfRestaurantSelectedUseCase();
        UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase = Injector.provideUpdateSelectedRestaurantUseCase();
        CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase = Injector.provideCreateNewSelectedRestaurantUseCase();

        // Création de la Factory avec les UseCases en paramètre
        RestaurantDetailViewModelFactory restaurantDetailViewModelFactory = new RestaurantDetailViewModelFactory(
                addToFavoritesUseCase,
                checkIfRestaurantSelectedUseCase,
                createNewSelectedRestaurantUseCase,
                updateSelectedRestaurantUseCase
        );

        // Obtention du ViewModel à partir de la Factory
        viewModel = new ViewModelProvider(this, restaurantDetailViewModelFactory).get(RestaurantDetailViewModel.class);

        // Récupération de l'objet Restaurant passé en extra
        restaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT_KEY);

        observeViewModel();
        setPicture();
        setNameAndAddress(restaurant);
        setRating();
        UpdateButton();
    }

    public void setPicture(){
        ImageLoader.loadRestaurantImage(binding.pictureRestaurantDetail, restaurant.getUrlPictureRestaurant());
    }

    private void setNameAndAddress(Restaurant restaurant) {
        binding.nameRestaurantDetail.setText(restaurant.getRestaurantName());
        binding.addressRestaurantDetail.setText(restaurant.getRestaurantAddress());
    }

    public void setRating() {
        RatingUtils.setRating(binding.itemListRestaurantRatingBar, restaurant.getRating());
    }

    public void UpdateButton() {

        binding.buttonLikeRestaurantDetail.setOnClickListener(view -> {
            if (restaurant != null) {
                viewModel.addRestaurantToFavorites(restaurant.getId());
            }
        });

        binding.buttonSelectedRestaurant.setOnClickListener(view -> {
            if (restaurant != null) {
                viewModel.createOrUpdateSelectedRestaurant(restaurant.getId());
            }
        });

        if (restaurant.getNumberPhone() != null) {
            binding.buttonCallRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext()
                    , restaurant.getNumberPhone(), Toast.LENGTH_SHORT).show());
        } else {
            binding.buttonCallRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext()
                    , "Unavailable number!", Toast.LENGTH_SHORT).show());
        }

        if (restaurant.getEmail() != null) {
            binding.buttonWebsiteRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext()
                    , restaurant.getEmail(), Toast.LENGTH_SHORT).show());
        } else {
            binding.buttonWebsiteRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext()
                    , "Unavailable website!", Toast.LENGTH_SHORT).show());
        }

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

        viewModel.getRestaurantSelected().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Restaurant ajouté aux selection avec succès!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}