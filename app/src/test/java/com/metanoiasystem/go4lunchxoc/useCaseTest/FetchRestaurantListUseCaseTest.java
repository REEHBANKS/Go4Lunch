package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.robolectric.Shadows.shadowOf;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchRestaurantListUseCase;
import com.metanoiasystem.go4lunchxoc.data.repository.RestaurantRepository;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.RepositoryFetchAllRestaurantFetchCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)

public class FetchRestaurantListUseCaseTest {

    private FetchRestaurantListUseCase useCase;

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @Mock
    private UseCaseCallback<List<Restaurant>> mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new FetchRestaurantListUseCase(mockRestaurantRepository);
    }

    @Test
    public void execute_whenRepositoryReturnsSuccess_invokesOnSuccess() {
        // Arrange
        List<Restaurant> mockRestaurants = new ArrayList<>();
        doAnswer(invocation -> {
            RepositoryFetchAllRestaurantFetchCallback callback = invocation.getArgument(2);
            callback.onSuccess(mockRestaurants);
            return null;
        }).when(mockRestaurantRepository).fetchRestaurant(anyDouble(), anyDouble(), any());

        // Act
        useCase.execute(0.0, 0.0, mockCallback);



        // Assert
        verify(mockCallback).onSuccess(mockRestaurants);
    }

    @Test
    public void execute_whenRepositoryReturnsError_invokesOnError() {
        // Arrange
        Throwable mockError = new Throwable("Test error");
        doAnswer(invocation -> {
            RepositoryFetchAllRestaurantFetchCallback callback = invocation.getArgument(2);
            callback.onError(mockError);
            return null;
        }).when(mockRestaurantRepository).fetchRestaurant(anyDouble(), anyDouble(), any());

        // Act
        useCase.execute(0.0, 0.0, mockCallback);

        shadowOf(getMainLooper()).idle();
        // Assert
        verify(mockCallback).onError(mockError);
    }
}

