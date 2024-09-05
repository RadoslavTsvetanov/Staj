package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.services.*;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlanControllerTest {

    @InjectMocks
    private PlanController planController;

    @Mock
    private PlanService planService;

    @Mock
    private UserService userService;

    @Mock
    private LocationService locationService;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceLocationService placeLocationService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String getAuthHeader(String token) {
        return "Bearer " + token;
    }

    @Test
    void getAllPlans_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<List<Plan>> response = planController.getAllPlans(getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAllPlans_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        List<Plan> plans = new ArrayList<>();
        when(planService.findAll()).thenReturn(plans);

        ResponseEntity<List<Plan>> response = planController.getAllPlans(getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plans, response.getBody());
    }

    @Test
    void getPlanById_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<Plan> response = planController.getPlanById(1L, getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getPlanById_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan plan = new Plan();
        when(planService.findById(1L)).thenReturn(plan);

        ResponseEntity<Plan> response = planController.getPlanById(1L, getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
    }

    @Test
    void createPlan_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        Plan plan = new Plan();
        ResponseEntity<Plan> response = planController.createPlan(plan, getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void createPlan_Conflict() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        Plan plan = new Plan();
        plan.setName("Existing Plan");
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(planService.userHasPlanWithName(username, plan.getName())).thenReturn(true);

        ResponseEntity<Plan> response = planController.createPlan(plan, getAuthHeader(token));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createPlan_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        Plan plan = new Plan();
        plan.setName("New Plan");
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(planService.userHasPlanWithName(username, plan.getName())).thenReturn(false);
        when(planService.save(any(Plan.class))).thenReturn(plan);

        ResponseEntity<Plan> response = planController.createPlan(plan, getAuthHeader(token));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(plan, response.getBody());
    }

    @Test
    void addUserToPlan_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<String> response = planController.addUserToPlan(1L, "username", getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addUserToPlan_Success() throws Exception {
        String token = "valid_token";
        String currentUsername = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);

        ResponseEntity<String> response = planController.addUserToPlan(1L, "username", getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added successfully.", response.getBody());
    }

    @Test
    void addPlacesToPlan_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<String> response = planController.addPlacesToPlan(1L, new ArrayList<>(), getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addPlacesToPlan_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan plan = new Plan();
        when(planService.addPlacesToPlan(anyLong(), anyList())).thenReturn(plan);

        ResponseEntity<String> response = planController.addPlacesToPlan(1L, new ArrayList<>(), getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Places added successfully.", response.getBody());
    }

    @Test
    void addLocationsToPlace_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<String> response = planController.addLocationsToPlace(1L, 1L, new ArrayList<>(), getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addLocationsToPlace_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan plan = new Plan();
        when(planService.findById(1L)).thenReturn(plan);

        Place place = new Place();
        when(placeService.findById(1L)).thenReturn(place);

        ResponseEntity<String> response = planController.addLocationsToPlace(1L, 1L, new ArrayList<>(), getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Locations added to place successfully.", response.getBody());
    }

    @Test
    void setDateWindowToPlan_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        ResponseEntity<Plan> response = planController.setDateWindowToPlan(1L, null, new DateWindow(), getAuthHeader(token));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void setDateWindowToPlan_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan plan = new Plan();
        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(LocalDate.now());
        dateWindow.setEndDate(LocalDate.now().plusDays(1));

        when(planService.findById(1L)).thenReturn(plan);
        when(planService.saveDateWindow(any(DateWindow.class))).thenReturn(dateWindow);
        when(planService.save(any(Plan.class))).thenReturn(plan);

        ResponseEntity<Plan> response = planController.setDateWindowToPlan(1L, null, dateWindow, getAuthHeader(token));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
    }


    @Test
    void updatePlan_Unauthorized() throws Exception {
        String token = "invalid_token";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        Plan updatedPlan = new Plan();
        ResponseEntity<?> response = planController.updatePlan(getAuthHeader(token), updatedPlan);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updatePlan_Success() throws Exception {
        String token = "valid_token";
        String username = "testuser";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan existingPlan = new Plan();
        Plan updatedPlan = new Plan();
        updatedPlan.setId(1L);
        updatedPlan.setName("Updated Plan");

        when(planService.isUserInPlan(1L, username)).thenReturn(true);
        when(planService.findById(1L)).thenReturn(existingPlan);
        when(planService.save(any(Plan.class))).thenReturn(existingPlan);

        ResponseEntity<?> response = planController.updatePlan(getAuthHeader(token), updatedPlan);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Plan updated successfully.", response.getBody());
    }
}

