package com.metanoiasystem.go4lunchxoc.useCaseTest;

import com.google.android.gms.maps.model.LatLng;
import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantFromSearchBarUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchOneRestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseFetchOneRestaurantCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class FetchRestaurantFromSearchBarUseCaseTest {

    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private UseCaseFetchOneRestaurantCallback<Restaurant> mockCallback;

    private FetchRestaurantFromSearchBarUseCase fetchRestaurantFromSearchBarUseCase;
    private LatLng testLatLng = new LatLng(0, 0);
    private String testId = "testId";
    private Float testRating = 4.5f;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fetchRestaurantFromSearchBarUseCase = new FetchRestaurantFromSearchBarUseCase(mockRestaurantRepository);
    }

    @Test
    public void execute_callsRepositoryWithCorrectParameters() {
        // Arrange
        doAnswer(invocation -> {
            RepositoryFetchOneRestaurantCallback<Restaurant> callback = invocation.getArgument(3);
            callback.onSuccess(new Restaurant()); // Mock response
            return null;
        }).when(mockRestaurantRepository).fetchOneRestaurantFromNetwork(any(LatLng.class), any(String.class), any(Float.class), any());

        // Act
        fetchRestaurantFromSearchBarUseCase.execute(testLatLng, testId, testRating, mockCallback);

        // Assert
        verify(mockRestaurantRepository).fetchOneRestaurantFromNetwork(eq(testLatLng), eq(testId), eq(testRating), any());
    }

    @Test
    public void execute_onSuccess_callsCallbackWithRestaurant() {
        // Arrange
        Restaurant mockRestaurant = new Restaurant();
        doAnswer(invocation -> {
            RepositoryFetchOneRestaurantCallback<Restaurant> callback = invocation.getArgument(3);
            callback.onSuccess(mockRestaurant); // Mock response
            return null;
        }).when(mockRestaurantRepository).fetchOneRestaurantFromNetwork(any(LatLng.class), any(String.class), any(Float.class), any());

        // Act
        fetchRestaurantFromSearchBarUseCase.execute(testLatLng, testId, testRating, mockCallback);

        // Assert
        verify(mockCallback).onSuccess(mockRestaurant);
    }

    @Test
    public void execute_onError_callsCallbackWithError() {
        // Arrange
        Throwable mockError = new Throwable();
        doAnswer(invocation -> {
            RepositoryFetchOneRestaurantCallback<Restaurant> callback = invocation.getArgument(3);
            callback.onError(mockError); // Mock error
            return null;
        }).when(mockRestaurantRepository).fetchOneRestaurantFromNetwork(any(LatLng.class), any(String.class), any(Float.class), any());

        // Act
        fetchRestaurantFromSearchBarUseCase.execute(testLatLng, testId, testRating, mockCallback);

        // Assert
        verify(mockCallback).onError(mockError);
    }
}

