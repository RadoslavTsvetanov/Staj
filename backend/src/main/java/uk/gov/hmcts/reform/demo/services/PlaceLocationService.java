package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.models.PlaceLocation;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.repositories.PlaceLocationRepo;
import uk.gov.hmcts.reform.demo.repositories.PlaceRepo;
import uk.gov.hmcts.reform.demo.repositories.LocationRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaceLocationService {

    @Autowired
    private PlaceLocationRepo placeLocationRepo;

    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private LocationRepo locationRepo;

    public PlaceLocation save(PlaceLocation placeLocation) {
        return placeLocationRepo.save(placeLocation);
    }

    public List<PlaceLocation> findAll() {
        return placeLocationRepo.findAll();
    }

    public void addLocationsToPlace(Long placeId, List<Long> locationIds) {
        Place place = placeRepo.findById(placeId)
            .orElseThrow(() -> new NoSuchElementException("Place not found with id " + placeId));

        for (Long locationId : locationIds) {
            Location location = locationRepo.findById(locationId)
                .orElseThrow(() -> new NoSuchElementException("Location not found with id " + locationId));

            PlaceLocation placeLocation = new PlaceLocation();
            placeLocation.setPlace(place);
            placeLocation.setLocation(location);
            placeLocationRepo.save(placeLocation);
        }
    }
}
