package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.services.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // Create a new location
    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationService.save(location);
    }

    // Get all locations
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    // Get a location by ID
    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Long id) {
        return locationService.findById(id);
    }

    // Get a location by name
    @GetMapping("/name/{name}")
    public Location getLocationByName(@PathVariable String name) {
        return locationService.findByName(name);
    }
}
