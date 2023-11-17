package com.metanoiasystem.go4lunchxoc.useCaseTest;

import static android.os.Looper.getMainLooper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.data.repository.SelectedRestaurantRepository;
import com.metanoiasystem.go4lunchxoc.domain.usecase.GetSelectedRestaurantsWithIdUseCase;
import com.metanoiasystem.go4lunchxoc.domain.interfaceUseCase.GetCurrentDateUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GetSelectedRestaurantsWithIdUseCaseTest {

    @Mock
    private SelectedRestaurantRepository mockRepository;
    @Mock
    private GetCurrentDateUseCase mockGetCurrentDateUseCase;

    private GetSelectedRestaurantsWithIdUseCase useCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new GetSelectedRestaurantsWithIdUseCase(mockRepository, mockGetCurrentDateUseCase);
    }

    @Test
    public void execute_returnsListOfSelectedRestaurants() throws Exception {
        // Arrange
        String restaurantId = "restaurant123";
        String currentDate = "11/11/23";
        when(mockGetCurrentDateUseCase.execute()).thenReturn(currentDate);

        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);
        List<SelectedRestaurant> expectedList = new ArrayList<>();
        expectedList.add(new SelectedRestaurant()); // Ajoutez des restaurants fictifs

        Task<QuerySnapshot> mockTask = Tasks.forResult(mockQuerySnapshot);
        when(mockQuerySnapshot.toObjects(SelectedRestaurant.class)).thenReturn(expectedList);
        when(mockRepository.getAllSelectedRestaurantsWithId(restaurantId, currentDate)).thenReturn(mockTask);

        // Act
        Task<List<SelectedRestaurant>> resultTask = useCase.execute(restaurantId);

        shadowOf(getMainLooper()).idle();
        // Assert
        assertTrue(resultTask.isSuccessful());
        assertEquals(expectedList, resultTask.getResult());
    }

    // Ajoutez d'autres tests pour les scénarios d'erreur
}

