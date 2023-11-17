package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckIfRestaurantSelectedUseCaseTest {

    @Mock
    private SelectedRestaurantRepository mockRepository;

    @Mock
    private Task<QuerySnapshot> mockTask;

    private CheckIfRestaurantSelectedUseCase useCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new CheckIfRestaurantSelectedUseCase(mockRepository);
    }

    @Test
    public void execute_callsRepositoryWithCorrectParameters() {
        // Arrange
        String userId = "user123";
        String date = "2023-11-09";
        when(mockRepository.checkIfRestaurantSelected(userId, date)).thenReturn(mockTask);

        // Act
        Task<QuerySnapshot> result = useCase.execute(userId, date);

        // Assert
        verify(mockRepository).checkIfRestaurantSelected(userId, date);
        assertEquals(mockTask, result);
    }
}

