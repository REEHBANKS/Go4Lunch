package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CountUsersForRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateSelectedRestaurantUseCase;

public class Injector {

    private static UserRepository userRepository = null;
    private static FavoriteRestaurantRepository favoriteRestaurantRepository = null;
    private static SelectedRestaurantRepository selectedRestaurantRepository = null;
    private static RestaurantRepository restaurantRepository = null;
    private static CreateUserUseCase createUserUseCase = null;
    private static FetchAllUsersUseCase fetchAllUsersUseCase = null;
    private static AddToFavoritesUseCase addToFavoritesUseCase = null;
    private static CheckIfRestaurantSelectedUseCase checkIfRestaurantSelectedUseCase = null;
    private static CreateNewSelectedRestaurantUseCase createNewSelectedRestaurantUseCase = null;
    private static UpdateSelectedRestaurantUseCase updateSelectedRestaurantUseCase = null;
    private static GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase = null;
    private static FetchRestaurantListUseCase fetchRestaurantListUseCase = null;
    private static CountUsersForRestaurantUseCase countUsersForRestaurantUseCase = null;
    private static GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase = null;


    public static synchronized UserRepository provideUserRepository() {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance();
        }
        return userRepository;
    }

    public static synchronized FavoriteRestaurantRepository provideFavoriteRestaurantRepository() {
        if (favoriteRestaurantRepository == null) {
            favoriteRestaurantRepository = FavoriteRestaurantRepository.getInstance();
        }
        return favoriteRestaurantRepository;
    }

    public static synchronized SelectedRestaurantRepository provideSelectedRestaurantRepository() {
        if (selectedRestaurantRepository == null) {
            selectedRestaurantRepository = SelectedRestaurantRepository.getInstance();
        }
        return selectedRestaurantRepository;
    }

    public static synchronized RestaurantRepository provideRestaurantRepository() {
        if (restaurantRepository == null) {
            restaurantRepository = RestaurantRepository.getInstance();
        }
        return restaurantRepository;
    }


    public static synchronized CreateUserUseCase provideCreateUserUseCase() {
        if (createUserUseCase == null) {
            createUserUseCase = new CreateUserUseCase(provideUserRepository());
        }
        return createUserUseCase;
    }

    public static synchronized FetchAllUsersUseCase provideFetchAllUsersUseCase() {
        if (fetchAllUsersUseCase == null) {
            fetchAllUsersUseCase = new FetchAllUsersUseCase(provideUserRepository());
        }
        return fetchAllUsersUseCase;
    }

    public static synchronized AddToFavoritesUseCase provideAddToFavoritesUseCase() {
        if (addToFavoritesUseCase == null) {
            addToFavoritesUseCase = new AddToFavoritesUseCase(provideFavoriteRestaurantRepository());
        }
        return addToFavoritesUseCase;
    }

    public static synchronized CreateNewSelectedRestaurantUseCase provideCreateNewSelectedRestaurantUseCase() {
        if (createNewSelectedRestaurantUseCase == null) {
            createNewSelectedRestaurantUseCase = new CreateNewSelectedRestaurantUseCase(provideSelectedRestaurantRepository());
        }
        return createNewSelectedRestaurantUseCase;
    }

    public static synchronized CheckIfRestaurantSelectedUseCase provideCheckIfRestaurantSelectedUseCase() {
        if (checkIfRestaurantSelectedUseCase == null) {
            checkIfRestaurantSelectedUseCase = new CheckIfRestaurantSelectedUseCase(provideSelectedRestaurantRepository());
        }
        return checkIfRestaurantSelectedUseCase;
    }

    public static synchronized UpdateSelectedRestaurantUseCase provideUpdateSelectedRestaurantUseCase() {
        if (updateSelectedRestaurantUseCase == null) {
            updateSelectedRestaurantUseCase = new UpdateSelectedRestaurantUseCase(provideSelectedRestaurantRepository());
        }
        return updateSelectedRestaurantUseCase;
    }

    public static synchronized GetSelectedRestaurantsWithIdUseCase provideGetSelectedRestaurantsWithIdUseCase(){
        if (getSelectedRestaurantsWithIdUseCase == null) {
            getSelectedRestaurantsWithIdUseCase = new GetSelectedRestaurantsWithIdUseCase(provideSelectedRestaurantRepository());
        }

        return getSelectedRestaurantsWithIdUseCase;
    }

    public static synchronized FetchRestaurantListUseCase provideFetchRestaurantListUseCase(){
        if (fetchRestaurantListUseCase == null) {
            fetchRestaurantListUseCase = new FetchRestaurantListUseCase();
        }

        return fetchRestaurantListUseCase;
    }

    public static synchronized CountUsersForRestaurantUseCase provideCountUsersForRestaurantUseCase(){
        if (countUsersForRestaurantUseCase == null) {
            countUsersForRestaurantUseCase = new CountUsersForRestaurantUseCase(provideSelectedRestaurantRepository());
        }

        return countUsersForRestaurantUseCase;
    }

    public static synchronized GetAllSelectedRestaurantsUseCase provideGetAllSelectedRestaurantsUseCase(){
        if (getAllSelectedRestaurantsUseCase == null) {
            getAllSelectedRestaurantsUseCase = new GetAllSelectedRestaurantsUseCase(provideSelectedRestaurantRepository());
        }

        return getAllSelectedRestaurantsUseCase;
    }








}


