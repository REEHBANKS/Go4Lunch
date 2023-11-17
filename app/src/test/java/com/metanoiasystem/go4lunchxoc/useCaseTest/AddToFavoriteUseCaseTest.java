package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.metanoiasystem.go4lunchxoc.data.repository.FavoriteRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.AddToFavoritesUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AddToFavoriteUseCaseTest {

    @Mock
    private FavoriteRestaurantRepository mockRepository;

    @Mock
    private Task<Void> mockTask;

    private AddToFavoritesUseCase addToFavoritesUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addToFavoritesUseCase = new AddToFavoritesUseCase(mockRepository);
    }

    @Test
    public void execute_callsCreateFavoriteRestaurant() {
        // Arrange
        String restaurantId = "restaurant_id";
        when(mockRepository.createFavoriteRestaurant(restaurantId)).thenReturn(mockTask);

        // Act
        Task<Void> result = addToFavoritesUseCase.execute(restaurantId);

        // Assert
        verify(mockRepository).createFavoriteRestaurant(restaurantId);
        assert result == mockTask; // Verify that the task returned is the one we mocked
    }
}
