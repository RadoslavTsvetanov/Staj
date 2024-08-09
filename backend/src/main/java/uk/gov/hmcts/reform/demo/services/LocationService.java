package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.repositories.LocationRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocationService {

    @Autowired
    private LocationRepo locationRepo;

    // Save a location
    public Location save(Location location) {
        return locationRepo.save(location);
    }

    // Find all locations
    public List<Location> findAll() {
        return locationRepo.findAll();
    }

    // Find location by ID
    public Location findById(Long id) {
        return locationRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Location not found with id " + id));
    }

    // Find location by name
    public Location findByName(String name) {
        return locationRepo.findByName(name);
    }
}
