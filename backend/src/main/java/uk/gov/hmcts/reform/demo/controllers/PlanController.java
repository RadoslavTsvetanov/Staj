package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.models.Location;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.LocationService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private JwtUtil jwtUtil;

    // Helper method to get the username from the current authentication
    private String getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(
        @Valid @RequestBody Plan plan,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        if (planService.findByName(plan.getName()).isPresent()) {
            return ResponseEntity.status(409).build(); // Conflict
        }

        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        User currentUser = userOptional.get();
        plan.getUsers().add(currentUser);

        // Handle DateWindow
        if (plan.getDateWindow() != null) {
            DateWindow dateWindow = plan.getDateWindow();
            DateWindow existingDateWindow = planService.findDateWindowByDates(dateWindow.getStartDate(), dateWindow.getEndDate());

            if (existingDateWindow != null) {
                plan.setDateWindow(existingDateWindow);
            } else {
                // Save the new DateWindow
                DateWindow savedDateWindow = planService.saveDateWindow(dateWindow);
                plan.setDateWindow(savedDateWindow);
            }
        }

        Plan savedPlan = planService.save(plan);
        return ResponseEntity.status(201).body(savedPlan); // Created
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<Plan> plans = planService.findAll();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(
        @PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Plan plan = planService.findById(id);
        if (plan == null) {
            return ResponseEntity.notFound().build(); // Not Found
        }
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/{planId}/users")
    public ResponseEntity<String> addUserToPlan(
        @PathVariable Long planId,
        @RequestParam String username,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized"); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(401).body("Unauthorized"); // Unauthorized
        }

        Optional<User> currentUser = userService.findByUsername(currentUsername);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(401).body("Current user not found."); // Unauthorized
        }

        Plan plan = planService.findById(planId);
        if (plan == null) {
            return ResponseEntity.status(404).body("Plan not found."); // Not Found
        }

        if (!plan.getUsers().contains(currentUser.get())) {
            return ResponseEntity.status(403).body("You must be a member of this plan to add other users."); // Forbidden
        }

        Optional<User> userToAdd = userService.findByUsername(username);
        if (userToAdd.isEmpty()) {
            return ResponseEntity.status(404).body("User not found."); // Not Found
        }

        planService.addUserToPlan(planId, username);
        return ResponseEntity.ok("User added successfully.");
    }

    @PutMapping("/{planId}/locations")
    public ResponseEntity<String> addLocationsToPlan(
        @PathVariable Long planId,
        @RequestBody List<String> locationNames,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized"); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized"); // Unauthorized
        }

        for (String locationName : locationNames) {
            Optional<Location> location = Optional.ofNullable(locationService.findByName(locationName));
            if (location.isEmpty()) {
                return ResponseEntity.status(404).body("Location not found: " + locationName); // Not Found
            }
        }

        Plan updatedPlan = planService.addLocationsToPlan(planId, locationNames);
        return ResponseEntity.ok("Locations added successfully.");
    }

    @PutMapping("/{planId}/date-window")
    public ResponseEntity<Plan> setDateWindowToPlan(
        @PathVariable Long planId,
        @RequestParam(required = false) Long dateWindowId,
        @RequestBody(required = false) DateWindow newDateWindow,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        Plan plan = planService.findById(planId);
        if (plan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Plan not found
        }

        if (dateWindowId != null) {
            Optional<DateWindow> existingDateWindowOpt = planService.findDateWindowById(dateWindowId);
            if (existingDateWindowOpt.isPresent()) {
                plan.setDateWindow(existingDateWindowOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // DateWindow not found
            }
        } else if (newDateWindow != null) {
            DateWindow savedDateWindow = planService.saveDateWindow(newDateWindow);
            plan.setDateWindow(savedDateWindow);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // No DateWindow provided
        }

        Plan updatedPlan = planService.save(plan);
        return ResponseEntity.ok(updatedPlan); // Return the updated Plan
    }

}

