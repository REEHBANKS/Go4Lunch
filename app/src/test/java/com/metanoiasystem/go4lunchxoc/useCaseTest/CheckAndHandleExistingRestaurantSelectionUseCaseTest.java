package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.robolectric.Shadows.shadowOf;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;

import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckAndHandleExistingRestaurantSelectionUseCaseImpl;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CheckIfRestaurantSelectedUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.CheckAndHandleExistingRestaurantSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.HandleExistingSelectionUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

public class CheckAndHandleExistingRestaurantSelectionUseCaseTest {

    private CheckAndHandleExistingRestaurantSelectionUseCase useCase;

    @Mock
    private CheckIfRestaurantSelectedUseCase mockCheckIfRestaurantSelectedUseCase;
    @Mock
    private HandleExistingSelectionUseCase mockHandleExistingSelectionUseCase;
    @Mock
    private SelectedUseCaseCallback mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new CheckAndHandleExistingRestaurantSelectionUseCaseImpl(mockCheckIfRestaurantSelectedUseCase, mockHandleExistingSelectionUseCase);
    }

    @Test
    public void execute_whenTaskSuccessful_callsHandleExistingSelectionUseCase() {
        // Arrange
        // Arrange
        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);
        Task<QuerySnapshot> successfulTask = Tasks.forResult(mockQuerySnapshot);
        when(mockCheckIfRestaurantSelectedUseCase.execute(anyString(), anyString())).thenReturn(successfulTask);

        // Act
        useCase.execute("restaurantId", "userId", "dateDeJour", mockCallback);

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockHandleExistingSelectionUseCase).execute(eq("restaurantId"), eq("userId"), eq("dateDeJour"), eq(successfulTask), eq(mockCallback));
    }

    @Test
    public void execute_whenTaskFails_invokesOnError() {
        // Arrange
        Exception exception = new Exception("Test exception");
        Task<QuerySnapshot> failedTask = Tasks.forException(exception);
        when(mockCheckIfRestaurantSelectedUseCase.execute(anyString(), anyString())).thenReturn(failedTask);

        // Act
        useCase.execute("restaurantId", "userId", "dateDeJour", mockCallback);

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockCallback).onError(exception);
    }
}

