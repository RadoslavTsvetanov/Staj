package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.repositories.LocationRepo;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    @Mock
    private LocationRepo locationRepo;

    @InjectMocks
    private LocationService locationService;

    private Location testLocation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setName("Test Location");
    }

    @Test
    public void testSave() {
        when(locationRepo.save(testLocation)).thenReturn(testLocation);

        Location savedLocation = locationService.save(testLocation);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getId()).isEqualTo(1L);
        assertThat(savedLocation.getName()).isEqualTo("Test Location");

        verify(locationRepo, times(1)).save(testLocation);
    }

    @Test
    public void testFindAll() {
        when(locationRepo.findAll()).thenReturn(Collections.singletonList(testLocation));

        List<Location> locationList = locationService.findAll();

        assertThat(locationList).isNotEmpty();
        assertThat(locationList.size()).isEqualTo(1);
        assertThat(locationList.get(0).getId()).isEqualTo(1L);

        verify(locationRepo, times(1)).findAll();
    }

    @Test
    public void testFindByIdSuccess() {
        when(locationRepo.findById(1L)).thenReturn(Optional.of(testLocation));

        Location foundLocation = locationService.findById(1L);

        assertThat(foundLocation).isNotNull();
        assertThat(foundLocation.getId()).isEqualTo(1L);

        verify(locationRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(locationRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            locationService.findById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Location not found with id 1");

        verify(locationRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByNameSuccess() {
        when(locationRepo.findByName("Test Location")).thenReturn(testLocation);

        Location foundLocation = locationService.findByName("Test Location");

        assertThat(foundLocation).isNotNull();
        assertThat(foundLocation.getName()).isEqualTo("Test Location");

        verify(locationRepo, times(1)).findByName("Test Location");
    }
}
