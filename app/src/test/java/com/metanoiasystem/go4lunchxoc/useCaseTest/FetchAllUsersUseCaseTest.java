package com.metanoiasystem.go4lunchxoc.useCaseTest;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchAllUsersUseCaseTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private Task<QuerySnapshot> mockTask;

    private FetchAllUsersUseCase fetchAllUsersUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fetchAllUsersUseCase = new FetchAllUsersUseCase(mockUserRepository);
        when(mockUserRepository.getAllUsersData()).thenReturn(mockTask);
    }

    @Test
    public void execute_returnsTaskFromRepository() {
        // Act
        Task<QuerySnapshot> result = fetchAllUsersUseCase.execute();

        // Assert
        verify(mockUserRepository).getAllUsersData();
        assert result == mockTask;
    }
}



