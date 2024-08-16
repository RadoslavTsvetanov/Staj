package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.services.LocationService;
import java.util.List;

@RestController
@RequestMapping("/locations")
@Validated
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        Location savedLocation = locationService.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.findById(id);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Location> getLocationByName(@PathVariable String name) {
        Location location = locationService.findByName(name);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }
}
