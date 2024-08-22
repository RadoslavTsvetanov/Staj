package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.PlaceRepo;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private PlanRepo planRepo;

    public Place save(Place place) {
        return placeRepo.save(place);
    }

    public Place findById(Long id) {
        return placeRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Place not found with id " + id));
    }

    public List<Place> findAll() {
        return placeRepo.findAll();
    }

    public Place addPlaceToPlan(Long planId, Place place) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        place = placeRepo.save(place);
        plan.getPlaces().add(place);
        planRepo.save(plan);

        return place;
    }

    public Optional<Place> findByName(String name) {
        return placeRepo.findByName(name);
    }

    public void deleteByName(String name) {
        Optional<Place> place = placeRepo.findByName(name);
        place.ifPresent(placeRepo::delete);
    }
}
