package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCompleteUserDataUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class GetCompleteUserDataUseCaseTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private CallbackUserUseCase mockCallback;

    private GetCompleteUserDataUseCase useCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new GetCompleteUserDataUseCase(mockUserRepository);
    }

    @Test
    public void execute_whenUserDataExists_invokesOnUserDataFetched() {
        // Arrange
        DocumentSnapshot mockDocumentSnapshot = mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.toObject(User.class)).thenReturn(new User()); // Remplacer par un utilisateur fictif si nécessaire
        Task<DocumentSnapshot> successfulTask = Tasks.forResult(mockDocumentSnapshot);
        when(mockUserRepository.getUserData()).thenReturn(successfulTask);

        // Act
        useCase.execute(mockCallback);

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockCallback).onUserDataFetched(any(User.class));
    }

    @Test
    public void execute_whenUserDataDoesNotExist_invokesOnUserDataFetchedWithNull() {
        // Arrange
        DocumentSnapshot mockDocumentSnapshot = mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Task<DocumentSnapshot> successfulTask = Tasks.forResult(mockDocumentSnapshot);
        when(mockUserRepository.getUserData()).thenReturn(successfulTask);

        // Act
        useCase.execute(mockCallback);

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockCallback).onUserDataFetched(null);
    }

    @Test
    public void execute_whenUserDataFetchFails_invokesOnError() {
        // Arrange
        Exception exception = new Exception("Test exception");
        Task<DocumentSnapshot> failedTask = Tasks.forException(exception);
        when(mockUserRepository.getUserData()).thenReturn(failedTask);

        // Act
        useCase.execute(mockCallback);

        shadowOf(getMainLooper()).idle();

        // Assert
        verify(mockCallback).onError(exception);
    }
}
