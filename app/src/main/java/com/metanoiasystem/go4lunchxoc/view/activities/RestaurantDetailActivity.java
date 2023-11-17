package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityRestaurantDetailBinding;
import com.metanoiasystem.go4lunchxoc.utils.AlarmHelper;
import com.metanoiasystem.go4lunchxoc.utils.ImageUtils;
import com.metanoiasystem.go4lunchxoc.utils.PreferencesManager;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
import com.metanoiasystem.go4lunchxoc.view.fragments.RestaurantSelectorListFragment;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantDetailViewModel;

import java.io.Serializable;


public class RestaurantDetailActivity extends AppCompatActivity {

    // Key for passing restaurant data through intent
    public static String RESTAURANT_KEY = "RESTAURANT_KEY";
    private RestaurantWithNumberUser restaurantWithNumberUser;
    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this activity
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        observeViewModel();

        // Initialize the view model
        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        // Retrieve the restaurant object passed as an extra
        Serializable extra = getIntent().getSerializableExtra(RESTAURANT_KEY);

        // Check the type of the passed object and assign it to restaurantWithNumberUser
        if (extra instanceof RestaurantWithNumberUser) {
            restaurantWithNumberUser = (RestaurantWithNumberUser) extra;
        } else if (extra instanceof Restaurant) {
            // Create a new RestaurantWithNumberUser or adapt your code to use Restaurant
            Restaurant restaurant = (Restaurant) extra;
            restaurantWithNumberUser = new RestaurantWithNumberUser(restaurant, 0); // Assuming 0 as the default number of users
        }

        // Check if restaurantWithNumberUser is not null and set up the UI accordingly
        if (restaurantWithNumberUser != null) {
            // Existing code to use restaurantWithNumberUser
            addRestaurantSelectorListFragment();
            setPicture();
            setNameAndAddress(restaurantWithNumberUser.getRestaurant());
            setRating();
            UpdateButton();
        } else {
            // Handle error or case where extra is not the expected type
        }
    }

    // Method to add a RestaurantSelectorListFragment to the activity
    private void addRestaurantSelectorListFragment() {
        // Create a bundle to pass the restaurant object to the fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESTAURANT_KEY, restaurantWithNumberUser);

        // Find the fragment by its ID or create a new instance and add it to the activity
        RestaurantSelectorListFragment fragment = (RestaurantSelectorListFragment) getSupportFragmentManager().findFragmentById(R.id.containerDetail);
        if (fragment != null) {
            // If the fragment already exists, set the restaurant object as an argument
            fragment.setArguments(bundle);
        } else {
            // If the fragment does not exist, create a new instance, set arguments, and add it
            fragment = new RestaurantSelectorListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.containerDetail, fragment).commit();
        }
    }

    // Method to set the restaurant image
    public void setPicture() {
        // Use ImageUtils to load the restaurant's image into the ImageView
        ImageUtils.loadRestaurantImage(binding.pictureRestaurantDetail, restaurantWithNumberUser.getRestaurant());
    }

    // Method to set the restaurant's name and address
    private void setNameAndAddress(Restaurant restaurant) {
        // Set the restaurant name and address in the corresponding TextViews
        binding.nameRestaurantDetail.setText(restaurant.getRestaurantName());
        binding.addressRestaurantDetail.setText(restaurant.getRestaurantAddress());
    }

    // Method to set the restaurant's rating
    public void setRating() {
        // Use RatingUtils to set the rating of the restaurant in the RatingBar
        RatingUtils.setRating(binding.itemListRestaurantRatingBar, restaurantWithNumberUser.getRestaurant().getRating());
    }

    // Method to update the Like button functionality
    public void UpdateButton() {
        // Set an OnClickListener for the Like button
        binding.buttonLikeRestaurantDetail.setOnClickListener(view -> {
            if (restaurantWithNumberUser != null) {
                // Call ViewModel to create a new favorite restaurant entry
                viewModel.createNewRestaurantFavorites(restaurantWithNumberUser.getRestaurant().getId());
            }
        });

        // Set the OnClickListener for the button to select the restaurant
        binding.buttonSelectedRestaurant.setOnClickListener(view -> {
            if (restaurantWithNumberUser != null) {
                // Call ViewModel to create or update the selected restaurant
                viewModel.createOrUpdateSelectedRestaurant(restaurantWithNumberUser.getRestaurant().getId());

                // Save the selected restaurant info in preferences
                PreferencesManager preferencesManager = new PreferencesManager(this);
                preferencesManager.saveRestaurantInfo(restaurantWithNumberUser.getRestaurant().getRestaurantName(),
                        restaurantWithNumberUser.getRestaurant().getRestaurantAddress());
            }
        });

// Set the OnClickListener for the call button
        if (restaurantWithNumberUser.getRestaurant().getNumberPhone() != null) {
            // Display the phone number in a toast if available
            binding.buttonCallRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext(),
                    restaurantWithNumberUser.getRestaurant().getNumberPhone(), Toast.LENGTH_SHORT).show());
        } else {
            // Display a message if the phone number is unavailable
            binding.buttonCallRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext(),
                    getString(R.string.unavailable_number), Toast.LENGTH_SHORT).show());
        }

// Set the OnClickListener for the website button
        if (restaurantWithNumberUser.getRestaurant().getEmail() != null) {
            // Display the email in a toast if available
            binding.buttonWebsiteRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext(),
                    restaurantWithNumberUser.getRestaurant().getEmail(), Toast.LENGTH_SHORT).show());
        } else {
            // Display a message if the website is unavailable
            binding.buttonWebsiteRestaurantDetail.setOnClickListener(v -> Toast.makeText(getApplicationContext(),
                    getString(R.string.unavailable_website), Toast.LENGTH_SHORT).show());
        }
    }

    // Observe the ViewModel for updates and success messages
    private void observeViewModel() {
        // Observe if adding to favorites was successful
        viewModel.getAddSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, getString(R.string.restaurant_added_favorites_success), Toast.LENGTH_SHORT).show();
            }
        });

        // Observe for error messages
        viewModel.errorMessage.observe(this, errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());

        // Observe if creating the selected restaurant was successful
        viewModel.isRestaurantCreated().observe(this, success -> {
            if (success) {
                Toast.makeText(this, getString(R.string.restaurant_added_selection_success), Toast.LENGTH_SHORT).show();

                // Retrieve saved restaurant info from SharedPreferences
                PreferencesManager preferencesManager = new PreferencesManager(this);
                String restaurantName = preferencesManager.getRestaurantName();
                String restaurantAddress = preferencesManager.getRestaurantAddress();

                // Configure an alarm if notifications are enabled
                if (preferencesManager.areNotificationsEnabled()) {
                    AlarmHelper alarmHelper = new AlarmHelper(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmHelper.configureAlarm(restaurantName, restaurantAddress);
                    }
                }
            }
        });

        // Observe if updating the selected restaurant was successful
        viewModel.isRestaurantUpdated().observe(this, success -> {
            if (success) {
                Toast.makeText(this, getString(R.string.restaurant_selection_modified), Toast.LENGTH_SHORT).show();
            }
        });
    }

}