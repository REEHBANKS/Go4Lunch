package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;

public class Injector {

    private static UserRepository userRepository = null;
    private static FavoriteRestaurantRepository favoriteRestaurantRepository = null;
    private static CreateUserUseCase createUserUseCase = null;
    private static FetchAllUsersUseCase fetchAllUsersUseCase = null;
    private static AddToFavoritesUseCase addToFavoritesUseCase = null;

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
}


