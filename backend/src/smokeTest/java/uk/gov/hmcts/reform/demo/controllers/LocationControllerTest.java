package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.services.LocationService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLocation_Success_ReturnsCreatedLocation() {
        Location location = new Location();
        Location savedLocation = new Location();
        savedLocation.setId(1L); // Assuming ID is generated

        when(locationService.save(any(Location.class))).thenReturn(savedLocation);

        ResponseEntity<Location> response = locationController.createLocation(location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedLocation, response.getBody());
        verify(locationService, times(1)).save(any(Location.class));
    }

    @Test
    void getAllLocations_Success_ReturnsLocations() {
        Location location1 = new Location();
        Location location2 = new Location();
        List<Location> locations = Arrays.asList(location1, location2);

        when(locationService.findAll()).thenReturn(locations);

        ResponseEntity<List<Location>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locations, response.getBody());
        verify(locationService, times(1)).findAll();
    }

    @Test
    void getLocationById_Success_ReturnsLocation() {
        Location location = new Location();
        Long id = 1L;

        when(locationService.findById(anyLong())).thenReturn(location);

        ResponseEntity<Location> response = locationController.getLocationById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());
        verify(locationService, times(1)).findById(anyLong());
    }

    @Test
    void getLocationById_NotFound_ReturnsNotFound() {
        Long id = 1L;

        when(locationService.findById(anyLong())).thenReturn(null);

        ResponseEntity<Location> response = locationController.getLocationById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(locationService, times(1)).findById(anyLong());
    }

    @Test
    void getLocationByName_Success_ReturnsLocation() {
        Location location = new Location();
        String name = "LocationName";

        when(locationService.findByName(name)).thenReturn(location);

        ResponseEntity<Location> response = locationController.getLocationByName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());
        verify(locationService, times(1)).findByName(name);
    }

    @Test
    void getLocationByName_NotFound_ReturnsNotFound() {
        String name = "LocationName";

        when(locationService.findByName(name)).thenReturn(null);

        ResponseEntity<Location> response = locationController.getLocationByName(name);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(locationService, times(1)).findByName(name);
    }
}
