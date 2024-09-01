package uk.gov.hmcts.reform.demo.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepo planRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private DateWindowRepo dateWindowRepo;

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceLocationService placeLocationService;

    public Plan save(Plan plan) {
        History history = new History();
        historyRepo.save(history);
        plan.setHistory(history);
        return planRepo.save(plan);
    }

    public Plan addPlacesToPlan(Long planId, List<Place> places) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        DateWindow planDateWindow = plan.getDateWindow();

        for (Place place : places) {
            DateWindow placeDateWindow = place.getDateWindow();

            if (placeDateWindow != null) {
                Long dateWindowId = place.getDateWindow().getId();
                if (dateWindowId == null || !dateWindowRepo.existsById(dateWindowId)) {
                    place.setDateWindow(dateWindowRepo.save(place.getDateWindow()));
                }
            }

            if (!isDateWindowWithinPlan(planDateWindow, placeDateWindow)) {
                throw new RuntimeException("Place date window is not within the plan's date window");
            }

            place.setPlan(plan);
            placeService.save(place);
            plan.getPlaces().add(place);
        }

        return planRepo.save(plan);
    }

    public boolean isDateWindowWithinPlan(DateWindow planDateWindow, DateWindow placeDateWindow) {
        if (planDateWindow == null || placeDateWindow == null) {
            return true;
        }

        LocalDate planStartDate = planDateWindow.getStartDate();
        LocalDate planEndDate = planDateWindow.getEndDate();
        LocalDate placeStartDate = placeDateWindow.getStartDate();
        LocalDate placeEndDate = placeDateWindow.getEndDate();

        return !placeStartDate.isBefore(planStartDate) && !placeEndDate.isAfter(planEndDate);
    }

    public Plan addLocationsToPlace(Long planId, Long placeId, List<String> locationNames) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        Place place = placeService.findById(placeId);
        DateWindow planDateWindow = plan.getDateWindow();
        DateWindow placeDateWindow = place.getDateWindow();

        if (!isDateWindowWithinPlan(planDateWindow, placeDateWindow)) {
            throw new RuntimeException("Place date window is not within the plan's date window");
        }

        List<Location> locations = locationNames.stream()
            .map(locationName -> locationRepo.findByName(locationName))
            .filter(location -> location != null)
            .toList();

        if (locations.size() != locationNames.size()) {
            throw new NoSuchElementException("One or more locations not found");
        }

        placeLocationService.addLocationsToPlace(placeId, locations.stream().map(Location::getId).toList());

        return planRepo.save(plan);
    }

    public Plan findById(Long id) {
        return planRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Plan not found with id " + id));
    }

    public Optional<Plan> findByName(String name) {
        return planRepo.findByName(name);
    }

    public List<Plan> findAll() {
        return planRepo.findAll();
    }

    public Plan setDateWindowToPlan(Long planId, Long dateWindowId, DateWindow newDateWindow) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        if (dateWindowId != null) {
            DateWindow existingDateWindow = dateWindowRepo.findById(dateWindowId)
                .orElseThrow(() -> new NoSuchElementException("DateWindow not found with id " + dateWindowId));
            plan.setDateWindow(existingDateWindow);
        } else {
            DateWindow savedDateWindow = dateWindowRepo.save(newDateWindow);
            plan.setDateWindow(savedDateWindow);
        }

        for (Place place : plan.getPlaces()) {
            DateWindow placeDateWindow = place.getDateWindow();
            if (placeDateWindow != null && !isDateWindowWithinPlan(plan.getDateWindow(), placeDateWindow)) {
                throw new RuntimeException("Place date window is not within the updated plan's date window");
            }
        }

        return planRepo.save(plan);
    }

    public List<Plan> findPlansByUsername(String username) {
        List<Plan> plans = planRepo.findPlansByUsername(username);
        for (Plan plan : plans) {
            Hibernate.initialize(plan.getDateWindow());
            Hibernate.initialize(plan.getUsernames());
        }
        return plans;
    }

    public boolean isUserInPlan(Long planId, String username) {
        Optional<Plan> planOptional = planRepo.findById(planId);
        if (planOptional.isEmpty()) {
            return false;
        }
        Plan plan = planOptional.get();
        return plan.getUsernames().contains(username);
    }

    public void removeUserFromAllPlans(String username) {
        List<Plan> plans = planRepo.findAll();
        for (Plan plan : plans) {
            if (plan.getUsernames().contains(username)) {
                plan.removeUsername(username);
                planRepo.save(plan);
            }
        }
    }

    public DateWindow findDateWindowByDates(LocalDate startDate, LocalDate endDate) {
        return dateWindowRepo.findByStartDateAndEndDate(startDate, endDate);
    }

    public DateWindow saveDateWindow(DateWindow dateWindow) {
        return dateWindowRepo.save(dateWindow);
    }

    public Optional<DateWindow> findDateWindowById(Long dateWindowId) {
        return dateWindowRepo.findById(dateWindowId);
    }

    public void addUserToPlan(Long planId, String username) {
        Optional<Plan> planOptional = planRepo.findById(planId);
        if (planOptional.isPresent()) {
            Plan plan = planOptional.get();
            plan.addUsername(username);
            planRepo.save(plan);
        }
    }

    public boolean userHasPlanWithName(String username, String planName) {
        return planRepo.findByUsernamesContainingAndName(username, planName).isPresent();
    }
}
