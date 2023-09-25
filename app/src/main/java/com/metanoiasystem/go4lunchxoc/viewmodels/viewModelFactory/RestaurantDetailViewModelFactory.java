package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantDetailViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantSelectorListViewModel;

public class RestaurantDetailViewModelFactory implements ViewModelProvider.Factory {

    private final AddToFavoritesUseCase addToFavoritesUseCase;
    private final CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;
    private final UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase;
    private final GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;


    public RestaurantDetailViewModelFactory(AddToFavoritesUseCase addToFavoritesUseCase,
                                            CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase,
                                            CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase,
                                            UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase,
                                            GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase,
                                            FetchAllUsersUseCase fetchAllUsersUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
        this.checkIfRestaurantSelectedUseCase = checkIfRestaurantSelectedUseCase;
        this.createNewSelectedRestaurantUseCase = createNewSelectedRestaurantUseCase;
        this.updateSelectedRestaurantUseCase = updateSelectedRestaurantUseCase;
        this.getSelectedRestaurantsWithIdUseCase = getSelectedRestaurantsWithIdUseCase;
        this.fetchAllUsersUseCase = fetchAllUsersUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            @SuppressWarnings("unchecked")
            T viewModel = (T) new RestaurantDetailViewModel(addToFavoritesUseCase,
                                                            checkIfRestaurantSelectedUseCase,
                                                            createNewSelectedRestaurantUseCase,
                                                            updateSelectedRestaurantUseCase,
                                                            getSelectedRestaurantsWithIdUseCase);
            return viewModel;
        } else if (modelClass.isAssignableFrom(RestaurantSelectorListViewModel.class)) {
            @SuppressWarnings("unchecked")
            T viewModel = (T) new RestaurantSelectorListViewModel(getSelectedRestaurantsWithIdUseCase,
                                                                    fetchAllUsersUseCase
                                                                    );
            return viewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
