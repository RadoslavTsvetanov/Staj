package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.LocationService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plans")
@Validated
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    private String getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) {
        if (planService.findByName(plan.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String username = getUsernameFromAuthentication();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userOptional.get();
        plan.getUsers().add(currentUser);

        Plan savedPlan = planService.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlan);
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planService.findAll();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long id) {
        Plan plan = planService.findById(id);
        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/{planId}/users")
    public ResponseEntity<String> addUserToPlan(@PathVariable Long planId, @RequestParam String username) {
        String currentUsername = getUsernameFromAuthentication();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current user not found.");
        }

        Optional<User> currentUser = userService.findByUsername(currentUsername);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current user not found.");
        }

        Plan plan = planService.findById(planId);
        if (plan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found.");
        }

        if (!plan.getUsers().contains(currentUser.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be a member of this plan to add other users.");
        }

        Optional<User> userToAdd = userService.findByUsername(username);
        if (userToAdd.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        planService.addUserToPlan(planId, username);
        return ResponseEntity.ok("User added successfully.");
    }

    @PutMapping("/{planId}/locations")
    public ResponseEntity<String> addLocationsToPlan(@PathVariable Long planId, @RequestBody List<String> locationNames) {
        for (String locationName : locationNames) {
            Optional<Location> location = Optional.ofNullable(locationService.findByName(locationName));
            if (location.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found: " + locationName);
            }
        }

        Plan updatedPlan = planService.addLocationsToPlan(planId, locationNames);
        return ResponseEntity.ok("Locations added successfully.");
    }

    @PutMapping("/{planId}/date-window")
    public ResponseEntity<Plan> setDateWindowToPlan(
        @PathVariable Long planId,
        @RequestParam(required = false) Long dateWindowId,
        @RequestBody(required = false) DateWindow newDateWindow
    ) {
        Plan updatedPlan = planService.setDateWindowToPlan(planId, dateWindowId, newDateWindow);
        return ResponseEntity.ok(updatedPlan);
    }
}
