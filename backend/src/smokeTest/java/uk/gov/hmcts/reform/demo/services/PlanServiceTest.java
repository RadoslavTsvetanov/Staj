package uk.gov.hmcts.reform.demo.services;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlanServiceTest {

    @Mock
    private PlanRepo planRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private DateWindowRepo dateWindowRepo;

    @Mock
    private HistoryRepo historyRepo;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceLocationService placeLocationService;

    @InjectMocks
    private PlanService planService;

    private Plan testPlan;
    private Place testPlace;
    private DateWindow testDateWindow;
    private History testHistory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testDateWindow = new DateWindow();
        testDateWindow.setStartDate(LocalDate.of(2024, 1, 1));
        testDateWindow.setEndDate(LocalDate.of(2024, 12, 31));

        testHistory = new History();

        testPlan = new Plan();
        testPlan.setId(1L);
        testPlan.setDateWindow(testDateWindow);
        testPlan.setHistory(testHistory);

        testPlace = new Place();
        testPlace.setId(1L);
        testPlace.setDateWindow(testDateWindow);
    }

//    @Test
//    public void testSave() {
//        History newHistory = new History();
//        when(historyRepo.save(any(History.class))).thenReturn(newHistory);
//        when(planRepo.save(testPlan)).thenReturn(testPlan);
//
//        Plan savedPlan = planService.save(testPlan);
//
//        assertThat(savedPlan.getHistory()).isSameAs(newHistory);
//
//        verify(historyRepo, times(1)).save(any(History.class));
//        verify(planRepo, times(1)).save(testPlan);
//    }

    @Test
    public void testAddPlacesToPlanSuccess() {
        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
        when(placeService.save(testPlace)).thenReturn(testPlace);
        when(planRepo.save(testPlan)).thenReturn(testPlan);

        Place placeToAdd = new Place();
        placeToAdd.setDateWindow(testDateWindow);
        List<Place> places = Collections.singletonList(placeToAdd);

        Plan updatedPlan = planService.addPlacesToPlan(1L, places);

        assertThat(updatedPlan.getPlaces()).contains(placeToAdd);

        verify(planRepo, times(1)).findById(1L);
        verify(placeService, times(1)).save(placeToAdd);
        verify(planRepo, times(1)).save(testPlan);
    }

    @Test
    public void testAddPlacesToPlanDateWindowNotWithinPlan() {
        DateWindow outsideDateWindow = new DateWindow();
        outsideDateWindow.setStartDate(LocalDate.of(2025, 1, 1));
        outsideDateWindow.setEndDate(LocalDate.of(2025, 12, 31));
        testPlace.setDateWindow(outsideDateWindow);

        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            planService.addPlacesToPlan(1L, Collections.singletonList(testPlace));
        });

        assertThat(thrown.getMessage()).isEqualTo("Place date window is not within the plan's date window");

        verify(planRepo, times(1)).findById(1L);
        verify(placeService, never()).save(testPlace);
        verify(planRepo, never()).save(testPlan);
    }

    @Test
    public void testIsDateWindowWithinPlan() {
        assertThat(planService.isDateWindowWithinPlan(testDateWindow, testDateWindow)).isTrue();

        DateWindow invalidDateWindow = new DateWindow();
        invalidDateWindow.setStartDate(LocalDate.of(2025, 1, 1));
        invalidDateWindow.setEndDate(LocalDate.of(2025, 12, 31));

        assertThat(planService.isDateWindowWithinPlan(testDateWindow, invalidDateWindow)).isFalse();
    }

    @Test
    public void testAddLocationsToPlaceSuccess() {
        List<String> locationNames = Arrays.asList("Location1", "Location2");
        Location location1 = new Location();
        location1.setId(1L);
        Location location2 = new Location();
        location2.setId(2L);

        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
        when(placeService.findById(1L)).thenReturn(testPlace);
        when(locationRepo.findByName("Location1")).thenReturn(location1);
        when(locationRepo.findByName("Location2")).thenReturn(location2);
        doNothing().when(placeLocationService).addLocationsToPlace(1L, Arrays.asList(1L, 2L));
        when(planRepo.save(testPlan)).thenReturn(testPlan);

        Plan updatedPlan = planService.addLocationsToPlace(1L, 1L, locationNames);

        verify(planRepo, times(1)).findById(1L);
        verify(placeService, times(1)).findById(1L);
        verify(locationRepo, times(2)).findByName(anyString());
        verify(placeLocationService, times(1)).addLocationsToPlace(1L, Arrays.asList(1L, 2L));
        verify(planRepo, times(1)).save(testPlan);
    }

    @Test
    public void testAddLocationsToPlaceLocationNotFound() {
        List<String> locationNames = Collections.singletonList("Nonexistent Location");

        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
        when(placeService.findById(1L)).thenReturn(testPlace);
        when(locationRepo.findByName("Nonexistent Location")).thenReturn(null);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            planService.addLocationsToPlace(1L, 1L, locationNames);
        });

        assertThat(thrown.getMessage()).isEqualTo("One or more locations not found");

        verify(planRepo, times(1)).findById(1L);
        verify(placeService, times(1)).findById(1L);
        verify(locationRepo, times(1)).findByName("Nonexistent Location");
        verify(placeLocationService, never()).addLocationsToPlace(anyLong(), anyList());
        verify(planRepo, never()).save(testPlan);
    }

    @Test
    public void testFindById() {
        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));

        Plan foundPlan = planService.findById(1L);

        assertThat(foundPlan).isNotNull();
        assertThat(foundPlan.getId()).isEqualTo(1L);

        verify(planRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(planRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            planService.findById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Plan not found with id 1");

        verify(planRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByName() {
        when(planRepo.findByName("Test Plan")).thenReturn(Optional.of(testPlan));

        Optional<Plan> foundPlan = planService.findByName("Test Plan");

        assertThat(foundPlan).isPresent();
        assertThat(foundPlan.get().getId()).isEqualTo(1L);

        verify(planRepo, times(1)).findByName("Test Plan");
    }

    @Test
    public void testFindAll() {
        when(planRepo.findAll()).thenReturn(Collections.singletonList(testPlan));

        List<Plan> plans = planService.findAll();

        assertThat(plans).isNotEmpty();
        assertThat(plans.size()).isEqualTo(1);
        assertThat(plans.get(0).getId()).isEqualTo(1L);

        verify(planRepo, times(1)).findAll();
    }

//    @Test
//    public void testSetDateWindowToPlan() {
//        DateWindow newDateWindow = new DateWindow();
//        newDateWindow.setStartDate(LocalDate.of(2024, 6, 1));
//        newDateWindow.setEndDate(LocalDate.of(2024, 12, 31));
//
//        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
//        when(dateWindowRepo.save(newDateWindow)).thenReturn(newDateWindow);
//        when(dateWindowRepo.findById(1L)).thenReturn(Optional.of(newDateWindow));
//        when(planRepo.save(testPlan)).thenReturn(testPlan);
//
//        Plan updatedPlan = planService.setDateWindowToPlan(1L, 1L, newDateWindow);
//
//        assertThat(updatedPlan.getDateWindow()).isEqualTo(newDateWindow);
//
//        verify(planRepo, times(1)).findById(1L);
//        verify(dateWindowRepo, times(1)).findById(1L);
//        verify(dateWindowRepo, times(1)).save(newDateWindow);
//        verify(planRepo, times(1)).save(testPlan);
//    }
//
//    @Test
//    public void testSetDateWindowToPlanInvalidDateWindow() {
//        DateWindow invalidDateWindow = new DateWindow();
//        invalidDateWindow.setStartDate(LocalDate.of(2025, 1, 1));
//        invalidDateWindow.setEndDate(LocalDate.of(2025, 12, 31));
//
//        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));
//        when(dateWindowRepo.findById(1L)).thenReturn(Optional.of(invalidDateWindow));
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//            planService.setDateWindowToPlan(1L, 1L, invalidDateWindow);
//        });
//
//        assertThat(thrown.getMessage()).isEqualTo("Place date window is not within the updated plan's date window");
//
//        verify(planRepo, times(1)).findById(1L);
//        verify(dateWindowRepo, times(1)).findById(1L);
//        verify(dateWindowRepo, never()).save(invalidDateWindow);
//    }

    @Test
    public void testFindPlansByUsername() {
        String username = "testuser";
        List<Plan> plans = Collections.singletonList(testPlan);

        when(planRepo.findPlansByUsername(username)).thenReturn(plans);

        List<Plan> foundPlans = planService.findPlansByUsername(username);

        assertThat(foundPlans).isNotEmpty();
        assertThat(foundPlans.get(0).getId()).isEqualTo(1L);

        verify(planRepo, times(1)).findPlansByUsername(username);
    }

    @Test
    public void testIsUserInPlan() {
        String username = "testuser";
        Set<String> usernames = new HashSet<>(Collections.singletonList(username));
        testPlan.setUsernames(usernames);

        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));

        boolean isUserInPlan = planService.isUserInPlan(1L, username);

        assertThat(isUserInPlan).isTrue();

        verify(planRepo, times(1)).findById(1L);
    }

    @Test
    public void testIsUserInPlanNotInPlan() {
        String username = "testuser";
        when(planRepo.findById(1L)).thenReturn(Optional.of(testPlan));

        boolean isUserInPlan = planService.isUserInPlan(1L, username);

        assertThat(isUserInPlan).isFalse();

        verify(planRepo, times(1)).findById(1L);
    }

    @Test
    public void testRemoveUserFromAllPlans() {
        String username = "testuser";
        Plan planWithUser = new Plan();
        planWithUser.setUsernames(new HashSet<>(Collections.singletonList(username)));
        Plan planWithoutUser = new Plan();
        planWithoutUser.setUsernames(new HashSet<>());

        when(planRepo.findAll()).thenReturn(Arrays.asList(planWithUser, planWithoutUser));
        when(planRepo.save(planWithUser)).thenReturn(planWithUser);

        planService.removeUserFromAllPlans(username);

        assertThat(planWithUser.getUsernames()).doesNotContain(username);

        verify(planRepo, times(1)).findAll();
        verify(planRepo, times(1)).save(planWithUser);
    }

    @Test
    public void testFindDateWindowByDates() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        when(dateWindowRepo.findByStartDateAndEndDate(startDate, endDate)).thenReturn(testDateWindow);

        DateWindow foundDateWindow = planService.findDateWindowByDates(startDate, endDate);

        assertThat(foundDateWindow).isEqualTo(testDateWindow);

        verify(dateWindowRepo, times(1)).findByStartDateAndEndDate(startDate, endDate);
    }

    @Test
    public void testSaveDateWindow() {
        when(dateWindowRepo.save(testDateWindow)).thenReturn(testDateWindow);

        DateWindow savedDateWindow = planService.saveDateWindow(testDateWindow);

        assertThat(savedDateWindow).isEqualTo(testDateWindow);

        verify(dateWindowRepo, times(1)).save(testDateWindow);
    }

    @Test
    public void testFindDateWindowById() {
        when(dateWindowRepo.findById(1L)).thenReturn(Optional.of(testDateWindow));

        Optional<DateWindow> foundDateWindow = planService.findDateWindowById(1L);

        assertThat(foundDateWindow).isPresent();
        assertThat(foundDateWindow.get()).isEqualTo(testDateWindow);

        verify(dateWindowRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindDateWindowByIdNotFound() {
        when(dateWindowRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<DateWindow> foundDateWindow = planService.findDateWindowById(1L);

        assertThat(foundDateWindow).isEmpty();

        verify(dateWindowRepo, times(1)).findById(1L);
    }

    @Test
    public void testAddUserToPlan() {
        String username = "testuser";
        Plan planWithUser = new Plan();
        planWithUser.setUsernames(new HashSet<>());

        when(planRepo.findById(1L)).thenReturn(Optional.of(planWithUser));
        when(planRepo.save(planWithUser)).thenReturn(planWithUser);

        planService.addUserToPlan(1L, username);

        assertThat(planWithUser.getUsernames()).contains(username);

        verify(planRepo, times(1)).findById(1L);
        verify(planRepo, times(1)).save(planWithUser);
    }

    @Test
    public void testUserHasPlanWithName() {
        String username = "testuser";
        String planName = "Test Plan";

        when(planRepo.findByUsernamesContainingAndName(username, planName)).thenReturn(Optional.of(testPlan));

        boolean hasPlan = planService.userHasPlanWithName(username, planName);

        assertThat(hasPlan).isTrue();

        verify(planRepo, times(1)).findByUsernamesContainingAndName(username, planName);
    }

    @Test
    public void testUserDoesNotHavePlanWithName() {
        String username = "testuser";
        String planName = "Nonexistent Plan";

        when(planRepo.findByUsernamesContainingAndName(username, planName)).thenReturn(Optional.empty());

        boolean hasPlan = planService.userHasPlanWithName(username, planName);

        assertThat(hasPlan).isFalse();

        verify(planRepo, times(1)).findByUsernamesContainingAndName(username, planName);
    }
}
