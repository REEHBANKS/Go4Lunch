package com.metanoiasystem.go4lunchxoc.utils;

import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckAndHandleExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantFromSearchBarUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantsWithSelectedUsersUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCompleteUserDataUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCurrentDateUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCurrentUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantForCurrentUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.HandleExistingSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateUserViewDrawerUseCase;

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
    private static UpdateExistingRestaurantSelectionUseCase updateExistingRestaurantSelectionUseCase = null;
    private static GetSelectedRestaurantsWithIdUseCase getSelectedRestaurantsWithIdUseCase = null;
    private static FetchRestaurantListUseCase fetchRestaurantListUseCase = null;
    private static GetAllSelectedRestaurantsUseCase getAllSelectedRestaurantsUseCase = null;
    private static GetAllRestaurantsFromFirebaseUseCase getAllRestaurantsFromFirebaseUseCase = null;
    private static CheckAndHandleExistingRestaurantSelectionUseCase checkAndHandleExistingRestaurantSelectionUseCase = null;
    private static HandleExistingSelectionUseCase handleExistingSelectionUseCase = null;
    private static GetCurrentUseCase getCurrentUseCase = null;
    private static GetCurrentDateUseCase getCurrentDateUseCase = null;
    private static FetchRestaurantsWithSelectedUsersUseCase fetchRestaurantsWithSelectedUsersUseCase = null;
    private static FetchRestaurantFromSearchBarUseCase fetchRestaurantFromSearchBarUseCase = null;
    private static GetCompleteUserDataUseCase getCompleteUserDataUseCase = null;
    private static UpdateUserViewDrawerUseCase updateUserViewDrawerUseCase = null;
    private static GetSelectedRestaurantForCurrentUserUseCase getSelectedRestaurantForCurrentUserUseCase = null;

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

    public static synchronized UpdateExistingRestaurantSelectionUseCase provideUpdateExistingRestaurantSelectionUseCase() {
        if (updateExistingRestaurantSelectionUseCase == null) {
            updateExistingRestaurantSelectionUseCase = new UpdateExistingRestaurantSelectionUseCaseImpl(provideSelectedRestaurantRepository());
        }
        return updateExistingRestaurantSelectionUseCase;
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


    public static synchronized GetAllSelectedRestaurantsUseCase provideGetAllSelectedRestaurantsUseCase(){
        if (getAllSelectedRestaurantsUseCase == null) {
            getAllSelectedRestaurantsUseCase = new GetAllSelectedRestaurantsUseCase();
        }

        return getAllSelectedRestaurantsUseCase;
    }

    public static synchronized GetAllRestaurantsFromFirebaseUseCase provideGetAllRestaurantsFromFirebaseUseCase(){
        if(getAllRestaurantsFromFirebaseUseCase == null){
           getAllRestaurantsFromFirebaseUseCase = new GetAllRestaurantsFromFirebaseUseCase(provideRestaurantRepository());
        }
        return getAllRestaurantsFromFirebaseUseCase;
    }

    public static synchronized CheckAndHandleExistingRestaurantSelectionUseCase provideCheckAndHandleExistingRestaurantSelectionUseCase(){
        if(checkAndHandleExistingRestaurantSelectionUseCase == null){
            checkAndHandleExistingRestaurantSelectionUseCase = new CheckAndHandleExistingRestaurantSelectionUseCaseImpl(provideCheckIfRestaurantSelectedUseCase(),
                                                                                                                            provideHandleExistingSelectionUseCase());
        }
        return checkAndHandleExistingRestaurantSelectionUseCase;
    }

    public static synchronized HandleExistingSelectionUseCase provideHandleExistingSelectionUseCase(){
        if(handleExistingSelectionUseCase == null){
            handleExistingSelectionUseCase = new HandleExistingSelectionUseCaseImpl(provideUpdateExistingRestaurantSelectionUseCase(),
                                                                                        provideCreateNewSelectedRestaurantUseCase());
        }
        return handleExistingSelectionUseCase;
    }

    public static synchronized GetCurrentUseCase provideGetCurrentUseCase(){
        if(getCurrentUseCase == null){
            getCurrentUseCase = new GetCurrentUseCaseImpl();
        }
        return getCurrentUseCase;
    }

    public static synchronized GetCurrentDateUseCase provideGetCurrentDateUseCase(){
        if(getCurrentDateUseCase == null){
            getCurrentDateUseCase = new GetCurrentDateUseCaseImpl();
        }
        return getCurrentDateUseCase;
    }

    public static synchronized  FetchRestaurantsWithSelectedUsersUseCase provideFetchRestaurantsWithSelectedUsersUseCase(){
        if(fetchRestaurantsWithSelectedUsersUseCase == null){
            fetchRestaurantsWithSelectedUsersUseCase = new FetchRestaurantsWithSelectedUsersUseCaseImpl(provideGetAllRestaurantsFromFirebaseUseCase(),
                    provideGetAllSelectedRestaurantsUseCase());
        }
        return fetchRestaurantsWithSelectedUsersUseCase;
    }

    public static synchronized FetchRestaurantFromSearchBarUseCase provideFetchRestaurantFromSearchBarUseCase(){
        if(fetchRestaurantFromSearchBarUseCase == null){
            fetchRestaurantFromSearchBarUseCase = new FetchRestaurantFromSearchBarUseCase();

        }
        return fetchRestaurantFromSearchBarUseCase;
    }

    public static synchronized GetCompleteUserDataUseCase provideGetCompleteUserDataUseCase(){
        if(getCompleteUserDataUseCase == null){
            getCompleteUserDataUseCase = new GetCompleteUserDataUseCase();
        }
        return getCompleteUserDataUseCase;
    }

    public static synchronized UpdateUserViewDrawerUseCase provideUpdateUserViewDrawerUseCase(){
        if(updateUserViewDrawerUseCase == null){
            updateUserViewDrawerUseCase = new UpdateUserViewDrawerUseCase();
        }
         return updateUserViewDrawerUseCase;
    }

    public static synchronized GetSelectedRestaurantForCurrentUserUseCase provideGetSelectedRestaurantForCurrentUserUseCase(){
        if(getSelectedRestaurantForCurrentUserUseCase == null){
            getSelectedRestaurantForCurrentUserUseCase = new GetSelectedRestaurantForCurrentUserUseCase();
        }
        return getSelectedRestaurantForCurrentUserUseCase;
    }




}











