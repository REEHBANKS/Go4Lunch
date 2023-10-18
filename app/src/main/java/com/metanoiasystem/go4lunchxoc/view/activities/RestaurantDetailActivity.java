package com.metanoiasystem.go4lunchxoc.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.ActivityRestaurantDetailBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.utils.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentUseCase;
import com.metanoiasystem.go4lunchxoc.utils.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.ImageUtils;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
import com.metanoiasystem.go4lunchxoc.view.fragments.RestaurantSelectorListFragment;
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
        CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase = Injector.provideCreateNewSelectedRestaurantUseCase();
        GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase = Injector.provideGetSelectedRestaurantsWithIdUseCase();
        FetchAllUsersUseCase fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();
        CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase = Injector.provideCheckAndHandleExistingRestaurantSelectionUseCase();
        GetCurrentUseCase getCurrentUseCase = Injector.provideGetCurrentUseCase();
        GetCurrentDateUseCase getCurrentDateUseCase = Injector.provideGetCurrentDateUseCase();

        // Création de la Factory avec les UseCases en paramètre
        RestaurantDetailViewModelFactory restaurantDetailViewModelFactory = new RestaurantDetailViewModelFactory(
                addToFavoritesUseCase,
                createNewSelectedRestaurantUseCase,
                getSelectedRestaurantsWithIdUseCase,
                fetchAllUsersUseCase,
                checkAndHandleExistingRestaurantSelectionUseCase,
                getCurrentUseCase, getCurrentDateUseCase
        );

        // Obtention du ViewModel à partir de la Factory
        viewModel = new ViewModelProvider(this, restaurantDetailViewModelFactory).get(RestaurantDetailViewModel.class);

        // Récupération de l'objet Restaurant passé en extra
        restaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT_KEY);

        if (restaurant != null) {

            addRestaurantSelectorListFragment();
        }



        observeViewModel();
        setPicture();
        setNameAndAddress(restaurant);
        setRating();
        UpdateButton();
    }

    private void addRestaurantSelectorListFragment(){
        // Passez l'objet Restaurant au Fragment

        Bundle bundle = new Bundle();
        bundle.putSerializable(RESTAURANT_KEY, restaurant);

        // Trouvez le fragment par son ID ou créez une nouvelle instance et ajoutez-le à l'activité
        RestaurantSelectorListFragment fragment = (RestaurantSelectorListFragment) getSupportFragmentManager().findFragmentById(R.id.containerDetail);
        if (fragment != null) {
            fragment.setArguments(bundle);
        } else {
            fragment = new RestaurantSelectorListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.containerDetail, fragment).commit();
        }

    }

    // Set Image

    public void setPicture(){
        ImageUtils.loadRestaurantImage(binding.pictureRestaurantDetail, restaurant);
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
                viewModel.createNewRestaurantFavorites(restaurant.getId());
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

        viewModel.isRestaurantCreated().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Restaurant ajouté aux selection avec succès!", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.isRestaurantUpdated().observe (this, success -> {
            if (success) {
                Toast.makeText(this, "Selection restaurant modifier!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}