package uk.gov.hmcts.reform.demo.services;

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

    public Plan save(Plan plan) {
        History history = new History();
        historyRepo.save(history); // Save the history to get the generated ID

        plan.setHistory(history); // Associate the new history with the plan
        return planRepo.save(plan);
    }

    public Plan addLocationsToPlan(Long planId, List<String> locationNames) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        for (String name : locationNames) {
            Location location = locationRepo.findByName(name);
            if (location == null) {
                throw new NoSuchElementException("Location not found with name " + name);
            }
            plan.getLocations().add(location);
        }

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

        return planRepo.save(plan);
    }

    public List<Plan> findPlansByUsername(String username) {
        return planRepo.findAllByUsernamesContaining(username);
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
}
