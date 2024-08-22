package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.services.PlaceService;

import java.util.Optional;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @PostMapping
    public ResponseEntity<Place> createPlace(@Valid @RequestBody Place place) {
        Place savedPlace = placeService.save(place);
        return ResponseEntity.status(201).body(savedPlace);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Optional<Place> place = Optional.ofNullable(placeService.findById(id));
        return place.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Place> getPlaceByName(@PathVariable String name) {
        Optional<Place> place = placeService.findByName(name);
        return place.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deletePlaceByName(@PathVariable String name) {
        placeService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }
}
