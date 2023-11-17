package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateNewSelectedRestaurantUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedUseCaseCallback;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

import android.util.Log;

// ... autres imports ...

@RunWith(RobolectricTestRunner.class)
public class CreateNewSelectedRestaurantUseCaseTest {

    private static final String TAG = "CreateNewSelectedRestaurantTest";

    private CreateNewSelectedRestaurantUseCase useCase;

    @Mock
    private SelectedRestaurantRepository mockRepository;

    @Mock
    private SelectedUseCaseCallback mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new CreateNewSelectedRestaurantUseCase(mockRepository);
        Log.d(TAG, "setUp: Test environment set up");
    }

    @Test
    public void execute_whenTaskSuccessful_invokesOnSuccess() {
        Log.d(TAG, "Starting test: execute_whenTaskSuccessful_invokesOnSuccess");
        // Arrange
        Task<Void> successfulTask = Tasks.forResult(null);
        doReturn(successfulTask).when(mockRepository).createNewSelectedRestaurant(any(), any(), any());
        Log.d(TAG, "Arranged: Mock repository configured for success scenario");

        // Act
        useCase.execute("restaurantId", "userId", "date", mockCallback);
        Log.d(TAG, "Acted: execute method called");

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockCallback).onSuccess();
        Log.d(TAG, "Asserted: onSuccess callback verified");
    }

    @Test
    public void execute_whenTaskFails_invokesOnError() {
        Log.d(TAG, "Starting test: execute_whenTaskFails_invokesOnError");
        // Arrange
        Exception exception = new Exception("Test exception");
        Task<Void> failedTask = Tasks.forException(exception);
        doReturn(failedTask).when(mockRepository).createNewSelectedRestaurant(any(), any(), any());
        Log.d(TAG, "Arranged: Mock repository configured for failure scenario");

        // Act
        useCase.execute("restaurantId", "userId", "date", mockCallback);
        Log.d(TAG, "Acted: execute method called");

        shadowOf(getMainLooper()).idle();
        // Assert
        verify(mockCallback).onError(exception);
        Log.d(TAG, "Asserted: onError callback verified");
    }
}
