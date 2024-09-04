package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.hmcts.reform.demo.controllers.PlaceController;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.services.DateWindowService;
import uk.gov.hmcts.reform.demo.services.PlaceService;
import uk.gov.hmcts.reform.demo.services.PlanService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlaceControllerTest {

    @InjectMocks
    private PlaceController placeController;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlanService planService;

    @Mock
    private DateWindowService dateWindowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlace_Success_ReturnsCreated() {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setName("Test Place");
        placeDTO.setPlanId(1L);
        placeDTO.setDateWindow(new DateWindowDTO(LocalDate.now(), LocalDate.now().plusDays(1)));

        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(placeDTO.getDateWindow().getStartDate());
        dateWindow.setEndDate(placeDTO.getDateWindow().getEndDate());

        Plan plan = new Plan();
        Place place = new Place();
        place.setName(placeDTO.getName());
        place.setPlan(plan);
        place.setDateWindow(dateWindow);

        when(dateWindowService.save(any(DateWindow.class))).thenReturn(dateWindow);
        when(planService.findById(placeDTO.getPlanId())).thenReturn(plan);
        when(placeService.save(any(Place.class))).thenReturn(place);

        ResponseEntity<Place> response = placeController.createPlace(placeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(place, response.getBody());
    }

    @Test
    void getPlaceById_Success_ReturnsPlace() {
        Place place = new Place();
        place.setId(1L);
        place.setName("Test Place");

        when(placeService.findById(1L)).thenReturn(place);

        ResponseEntity<Place> response = placeController.getPlaceById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place, response.getBody());
    }

    @Test
    void getPlaceById_NotFound_ReturnsNotFound() {
        when(placeService.findById(1L)).thenReturn(null);

        ResponseEntity<Place> response = placeController.getPlaceById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPlaceByName_Success_ReturnsPlace() {
        Place place = new Place();
        place.setName("Test Place");

        when(placeService.findByName("Test Place")).thenReturn(Optional.of(place));

        ResponseEntity<Place> response = placeController.getPlaceByName("Test Place");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place, response.getBody());
    }

    @Test
    void getPlaceByName_NotFound_ReturnsNotFound() {
        when(placeService.findByName("Test Place")).thenReturn(Optional.empty());

        ResponseEntity<Place> response = placeController.getPlaceByName("Test Place");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePlaceByName_Success_ReturnsNoContent() {
        ResponseEntity<Void> response = placeController.deletePlaceByName("Test Place");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(placeService, times(1)).deleteByName("Test Place");
    }
}
