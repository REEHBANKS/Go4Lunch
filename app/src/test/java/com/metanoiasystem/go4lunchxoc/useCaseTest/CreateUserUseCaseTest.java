package com.metanoiasystem.go4lunchxoc.useCaseTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import com.metanoiasystem.go4lunchxoc.data.repository.UserRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;

public class CreateUserUseCaseTest {

    @Mock
    private UserRepository mockUserRepository;

    private CreateUserUseCase createUserUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        createUserUseCase = new CreateUserUseCase(mockUserRepository);
    }

    @Test
    public void execute_callsCreateUser() {
        // Act
        createUserUseCase.execute();

        // Assert
        verify(mockUserRepository).createUser();
    }
}

