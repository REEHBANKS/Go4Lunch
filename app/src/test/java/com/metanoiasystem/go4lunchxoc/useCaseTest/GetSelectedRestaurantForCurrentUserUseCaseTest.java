package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllRestaurantsFromFirebaseUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetAllSelectedRestaurantsUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetCompleteUserDataUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantForCurrentUserUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.SelectedRestaurantCallback;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.UseCaseCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GetSelectedRestaurantForCurrentUserUseCaseTest {

    @Mock
    private GetCompleteUserDataUseCase mockGetUserDataUseCase;
    @Mock
    private GetAllSelectedRestaurantsUseCase mockGetAllSelectedRestaurantsUseCase;
    @Mock
    private GetAllRestaurantsFromFirebaseUseCase mockGetAllRestaurantsFromFirebaseUseCase;
    @Mock
    private SelectedRestaurantCallback mockCallback;

    private GetSelectedRestaurantForCurrentUserUseCase useCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new GetSelectedRestaurantForCurrentUserUseCase(
                mockGetUserDataUseCase,
                mockGetAllSelectedRestaurantsUseCase,
                mockGetAllRestaurantsFromFirebaseUseCase
        );
    }

    @Test
    public void execute_whenUserHasSelectedRestaurant_invokesOnRestaurantFound() {
        // Arrange
        String dateDuJour = "10/11/23";
        User mockUser = new User(); // Configurez votre utilisateur fictif
        mockUser.setUid("user123");

        SelectedRestaurant mockSelectedRestaurant = new SelectedRestaurant();
        mockSelectedRestaurant.setUserId("user123");
        mockSelectedRestaurant.setRestaurantId("restaurant123");

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId("restaurant123");

        // Simuler le comportement des cas d'utilisation
        // Simuler GetCompleteUserDataUseCase
        doAnswer(invocation -> {
            CallbackUserUseCase<User> callback = invocation.getArgument(0);
            callback.onUserDataFetched(mockUser);
            return null;
        }).when(mockGetUserDataUseCase).execute(any());

        // Simuler GetAllSelectedRestaurantsUseCase
        doAnswer(invocation -> {
            UseCaseCallback<List<SelectedRestaurant>> callback = invocation.getArgument(1);
            callback.onSuccess(Collections.singletonList(mockSelectedRestaurant));
            return null;
        }).when(mockGetAllSelectedRestaurantsUseCase).execute(eq(dateDuJour), any());

        // Simuler GetAllRestaurantsFromFirebaseUseCase
        doAnswer(invocation -> {
            UseCaseCallback<List<Restaurant>> callback = invocation.getArgument(0);
            callback.onSuccess(Collections.singletonList(mockRestaurant));
            return null;
        }).when(mockGetAllRestaurantsFromFirebaseUseCase).execute(any());

        // Act
        useCase.execute(dateDuJour, mockCallback);

        shadowOf(getMainLooper()).idle();
        // Assert
        verify(mockCallback).onRestaurantFound(mockRestaurant);
    }

    // Ajoutez d'autres tests pour les scénarios d'erreur et de non-trouvaille
}
