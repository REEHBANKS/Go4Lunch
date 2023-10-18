package com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.utils.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentDateUseCase;
import com.metanoiasystem.go4lunchxoc.utils.GetCurrentUseCase;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantDetailViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.RestaurantSelectorListViewModel;

public class RestaurantDetailViewModelFactory implements ViewModelProvider.Factory {

    private final AddToFavoritesUseCase addToFavoritesUseCase;
    private final CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase;
    private final GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;
    private final CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase;
    private final GetCurrentUseCase getCurrentUseCase;
    private final GetCurrentDateUseCase getCurrentDateUseCase;


    public RestaurantDetailViewModelFactory(AddToFavoritesUseCase addToFavoritesUseCase,
                                            CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase,
                                            GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase,
                                            FetchAllUsersUseCase fetchAllUsersUseCase,
                                            CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase,
                                            GetCurrentUseCase getCurrentUseCase,
                                            GetCurrentDateUseCase getCurrentDateUseCase) {
        this.addToFavoritesUseCase = addToFavoritesUseCase;
        this.createNewSelectedRestaurantUseCase = createNewSelectedRestaurantUseCase;
        this.getSelectedRestaurantsWithIdUseCase = getSelectedRestaurantsWithIdUseCase;
        this.fetchAllUsersUseCase = fetchAllUsersUseCase;
        this.checkAndHandleExistingRestaurantSelectionUseCase = checkAndHandleExistingRestaurantSelectionUseCase;
        this.getCurrentUseCase = getCurrentUseCase;
        this.getCurrentDateUseCase = getCurrentDateUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            @SuppressWarnings("unchecked")
            T viewModel = (T) new RestaurantDetailViewModel(addToFavoritesUseCase,
                                                            createNewSelectedRestaurantUseCase,
                                                            checkAndHandleExistingRestaurantSelectionUseCase,
                                                            getCurrentUseCase,
                                                            getCurrentDateUseCase);
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
