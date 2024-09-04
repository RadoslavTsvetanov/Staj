package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.models.PlaceLocation;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.repositories.PlaceLocationRepo;
import uk.gov.hmcts.reform.demo.repositories.PlaceRepo;
import uk.gov.hmcts.reform.demo.repositories.LocationRepo;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlaceLocationServiceTest {

    @Mock
    private PlaceLocationRepo placeLocationRepo;

    @Mock
    private PlaceRepo placeRepo;

    @Mock
    private LocationRepo locationRepo;

    @InjectMocks
    private PlaceLocationService placeLocationService;

    private PlaceLocation testPlaceLocation;
    private Place testPlace;
    private Location testLocation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testPlace = new Place();
        testPlace.setId(1L);

        testLocation = new Location();
        testLocation.setId(1L);

        testPlaceLocation = new PlaceLocation();
        testPlaceLocation.setPlace(testPlace);
        testPlaceLocation.setLocation(testLocation);
    }

    @Test
    public void testSave() {
        when(placeLocationRepo.save(testPlaceLocation)).thenReturn(testPlaceLocation);

        PlaceLocation savedPlaceLocation = placeLocationService.save(testPlaceLocation);

        assertThat(savedPlaceLocation).isNotNull();
        assertThat(savedPlaceLocation.getPlace()).isEqualTo(testPlace);
        assertThat(savedPlaceLocation.getLocation()).isEqualTo(testLocation);

        verify(placeLocationRepo, times(1)).save(testPlaceLocation);
    }

    @Test
    public void testFindAll() {
        when(placeLocationRepo.findAll()).thenReturn(Collections.singletonList(testPlaceLocation));

        List<PlaceLocation> placeLocations = placeLocationService.findAll();

        assertThat(placeLocations).isNotEmpty();
        assertThat(placeLocations.size()).isEqualTo(1);
        assertThat(placeLocations.get(0).getPlace()).isEqualTo(testPlace);
        assertThat(placeLocations.get(0).getLocation()).isEqualTo(testLocation);

        verify(placeLocationRepo, times(1)).findAll();
    }

    @Test
    public void testAddLocationsToPlace() {
        when(placeRepo.findById(1L)).thenReturn(Optional.of(testPlace));
        when(locationRepo.findById(1L)).thenReturn(Optional.of(testLocation));
        when(placeLocationRepo.save(any(PlaceLocation.class))).thenReturn(testPlaceLocation);

        placeLocationService.addLocationsToPlace(1L, Collections.singletonList(1L));

        verify(placeRepo, times(1)).findById(1L);
        verify(locationRepo, times(1)).findById(1L);
        verify(placeLocationRepo, times(1)).save(any(PlaceLocation.class));
    }

    @Test
    public void testAddLocationsToPlacePlaceNotFound() {
        when(placeRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            placeLocationService.addLocationsToPlace(1L, Collections.singletonList(1L));
        });

        assertThat(thrown.getMessage()).isEqualTo("Place not found with id 1");

        verify(placeRepo, times(1)).findById(1L);
        verify(locationRepo, never()).findById(anyLong());
        verify(placeLocationRepo, never()).save(any(PlaceLocation.class));
    }

    @Test
    public void testAddLocationsToPlaceLocationNotFound() {
        when(placeRepo.findById(1L)).thenReturn(Optional.of(testPlace));
        when(locationRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            placeLocationService.addLocationsToPlace(1L, Collections.singletonList(1L));
        });

        assertThat(thrown.getMessage()).isEqualTo("Location not found with id 1");

        verify(placeRepo, times(1)).findById(1L);
        verify(locationRepo, times(1)).findById(1L);
        verify(placeLocationRepo, never()).save(any(PlaceLocation.class));
    }
}
