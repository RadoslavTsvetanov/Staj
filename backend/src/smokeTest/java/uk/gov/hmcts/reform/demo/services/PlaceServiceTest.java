package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.PlaceRepo;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlaceServiceTest {

    @Mock
    private PlaceRepo placeRepo;

    @Mock
    private PlanRepo planRepo;

    @InjectMocks
    private PlaceService placeService;

    private Place testPlace;
    private Plan testPlan;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        DateWindow testDateWindow = new DateWindow();
        testDateWindow.setStartDate(LocalDate.of(2024, 1, 1));
        testDateWindow.setEndDate(LocalDate.of(2024, 12, 31));

        testPlace = new Place();
        testPlace.setId(1L);
        testPlace.setDateWindow(testDateWindow);

        testPlan = new Plan();
        testPlan.setId(1L);
        testPlan.setDateWindow(testDateWindow);
        testPlan.setPlaces(new ArrayList<>(Collections.singletonList(testPlace)));
    }


    @Test
    public void testSave() {
        when(placeRepo.save(testPlace)).thenReturn(testPlace);

        Place savedPlace = placeService.save(testPlace);

        assertThat(savedPlace).isNotNull();
        assertThat(savedPlace.getId()).isEqualTo(testPlace.getId());

        verify(placeRepo, times(1)).save(testPlace);
    }

    @Test
    public void testFindById() {
        when(placeRepo.findById(1L)).thenReturn(Optional.of(testPlace));

        Place foundPlace = placeService.findById(1L);

        assertThat(foundPlace).isNotNull();
        assertThat(foundPlace.getId()).isEqualTo(1L);

        verify(placeRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(placeRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            placeService.findById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Place not found with id 1");

        verify(placeRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        when(placeRepo.findAll()).thenReturn(Collections.singletonList(testPlace));

        List<Place> places = placeService.findAll();

        assertThat(places).isNotEmpty();
        assertThat(places.size()).isEqualTo(1);
        assertThat(places.get(0).getId()).isEqualTo(testPlace.getId());

        verify(placeRepo, times(1)).findAll();
    }

    @Test
    public void testAddPlaceToPlanSuccess() {
        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
        when(placeRepo.save(testPlace)).thenReturn(testPlace);

        Place addedPlace = placeService.addPlaceToPlan(1L, testPlace);

        assertThat(addedPlace).isNotNull();
        assertThat(addedPlace.getId()).isEqualTo(testPlace.getId());

        verify(planRepo, times(1)).findById(1L);
        verify(placeRepo, times(1)).save(testPlace);
        verify(planRepo, times(1)).save(testPlan);
    }

    @Test
    public void testAddPlaceToPlanDateWindowNotWithinPlan() {
        DateWindow outsideDateWindow = new DateWindow();
        outsideDateWindow.setStartDate(LocalDate.of(2025, 1, 1));
        outsideDateWindow.setEndDate(LocalDate.of(2025, 12, 31));
        testPlace.setDateWindow(outsideDateWindow);

        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            placeService.addPlaceToPlan(1L, testPlace);
        });

        assertThat(thrown.getMessage()).isEqualTo("The Place's DateWindow is not within the Plan's DateWindow.");

        verify(planRepo, times(1)).findById(1L);
        verify(placeRepo, never()).save(testPlace);
        verify(planRepo, never()).save(testPlan);
    }

    @Test
    public void testFindByName() {
        when(placeRepo.findByName("Test Place")).thenReturn(Optional.of(testPlace));

        Optional<Place> foundPlace = placeService.findByName("Test Place");

        assertThat(foundPlace).isPresent();
        assertThat(foundPlace.get().getId()).isEqualTo(testPlace.getId());

        verify(placeRepo, times(1)).findByName("Test Place");
    }

    @Test
    public void testDeleteByName() {
        when(placeRepo.findByName("Test Place")).thenReturn(Optional.of(testPlace));

        placeService.deleteByName("Test Place");

        verify(placeRepo, times(1)).findByName("Test Place");
        verify(placeRepo, times(1)).delete(testPlace);
    }

    @Test
    public void testDeleteByNameNotFound() {
        when(placeRepo.findByName("Nonexistent Place")).thenReturn(Optional.empty());

        placeService.deleteByName("Nonexistent Place");

        verify(placeRepo, times(1)).findByName("Nonexistent Place");
        verify(placeRepo, never()).delete(any(Place.class));
    }
}
