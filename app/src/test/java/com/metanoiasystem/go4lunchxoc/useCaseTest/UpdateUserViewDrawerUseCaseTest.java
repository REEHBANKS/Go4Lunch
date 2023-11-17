package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCompleteUserDataUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.UpdateUserViewDrawerUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class UpdateUserViewDrawerUseCaseTest {

    @Mock
    private GetCompleteUserDataUseCase getCompleteUserDataUseCase;

    @Mock
    private NavigationView navigationView;

    @Mock
    private View headerView;

    @Mock
    private TextView navUserName;

    @Mock
    private TextView navUserMail;

    private UpdateUserViewDrawerUseCase updateUserViewDrawerUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        updateUserViewDrawerUseCase = new UpdateUserViewDrawerUseCase(getCompleteUserDataUseCase);

        when(navigationView.getHeaderView(0)).thenReturn(headerView);
        when(headerView.findViewById(R.id.name_nav_header)).thenReturn(navUserName);
        when(headerView.findViewById(R.id.mail_nav_header)).thenReturn(navUserMail);
    }

    @Test
    public void updateUserView_updatesNavigationViewWithUserData() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("Test User");
        mockUser.setUserMail("test@example.com");

        // Act
        doAnswer(invocation -> {
            CallbackUserUseCase<User> callback = invocation.getArgument(0);
            callback.onUserDataFetched(mockUser);
            return null;
        }).when(getCompleteUserDataUseCase).execute(any());

        updateUserViewDrawerUseCase.updateUserView(navigationView);

        shadowOf(getMainLooper()).idle();
        // Assert
        verify(navUserName).setText("Test User");
        verify(navUserMail).setText("test@example.com");
    }
}
