package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.models.Place;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.PlaceRepo;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;

import java.time.LocalDate;
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

        if (isPlaceDateWindowWithinPlanDateWindow(plan, place)) {
            place = placeRepo.save(place);
            plan.getPlaces().add(place);
            planRepo.save(plan);
        } else {
            throw new IllegalArgumentException("The Place's DateWindow is not within the Plan's DateWindow.");
        }

        return place;
    }

    public Optional<Place> findByName(String name) {
        return placeRepo.findByName(name);
    }

    public void deleteByName(String name) {
        Optional<Place> place = placeRepo.findByName(name);
        place.ifPresent(placeRepo::delete);
    }

    private boolean isPlaceDateWindowWithinPlanDateWindow(Plan plan, Place place) {
        if (plan.getDateWindow() == null || place.getDateWindow() == null) {
            return true;
        }

        DateWindow planDateWindow = plan.getDateWindow();
        DateWindow placeDateWindow = place.getDateWindow();

        LocalDate planStartDate = planDateWindow.getStartDate();
        LocalDate planEndDate = planDateWindow.getEndDate();
        LocalDate placeStartDate = placeDateWindow.getStartDate();
        LocalDate placeEndDate = placeDateWindow.getEndDate();

        return !(placeStartDate.isBefore(planStartDate) || placeEndDate.isAfter(planEndDate));
    }
}
