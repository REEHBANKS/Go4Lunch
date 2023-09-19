package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityRestaurantDetailBinding;

public class RestaurantDetailActivity extends AppCompatActivity {

    public static String RESTAURANT_KEY = "RESTAURANT_KEY";
    private Restaurant restaurant;
    private ActivityRestaurantDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Récupération de l'objet Restaurant passé en extra
        restaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT_KEY);

        updateUi();
    }

    public void updateUi() {
        // Mettez à jour l'UI en fonction de l'objet 'restaurant'

        binding.nameRestaurantDetail.setText(restaurant.getRestaurantName());
    }
}