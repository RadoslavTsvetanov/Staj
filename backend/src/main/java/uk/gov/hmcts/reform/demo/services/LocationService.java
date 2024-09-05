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

    public Location save(Location location) {
        return locationRepo.save(location);
    }

    public List<Location> findAll() {
        return locationRepo.findAll();
    }

    public Location findById(Long id) {
        return locationRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Location not found with id " + id));
    }

    public Location findByName(String name) {
        return locationRepo.findByName(name);
    }
}
