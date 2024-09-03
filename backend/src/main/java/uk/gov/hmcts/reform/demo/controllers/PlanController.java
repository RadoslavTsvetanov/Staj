package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.services.*;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private PlaceService placeService;

    @Autowired
    private PlaceLocationService placeLocationService;

    @Autowired
    private JwtUtil jwtUtil;

    private String getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build();
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
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build();
        }

        Plan plan = planService.findById(id);
        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(
        @Valid @RequestBody Plan plan,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (planService.userHasPlanWithName(username, plan.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        plan.addUsername(username);
        Plan savedPlan = planService.save(plan);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlan);
    }

    @PostMapping("/{planId}/users")
    public ResponseEntity<String> addUserToPlan(
        @PathVariable Long planId,
        @RequestParam String username,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        planService.addUserToPlan(planId, username);
        return ResponseEntity.ok("User added successfully.");
    }

    @PutMapping("/{planId}/places")
    public ResponseEntity<String> addPlacesToPlan(
        @PathVariable Long planId,
        @RequestBody List<Place> places,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            Plan updatedPlan = planService.addPlacesToPlan(planId, places);
            return ResponseEntity.ok("Places added successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{planId}/places/{placeId}/locations")
    public ResponseEntity<String> addLocationsToPlace(
        @PathVariable Long planId,
        @PathVariable Long placeId,
        @RequestBody List<Location> locations,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        // Authorization check
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Plan plan = planService.findById(planId);
        if (plan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
        }

        Place place = placeService.findById(placeId);
        if (place == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found");
        }

        for (Location location : locations) {
            location = locationService.save(location);

            place.getPlaceLocations().add(new PlaceLocation(place, location));
        }

        placeService.save(place);

        return ResponseEntity.ok("Locations added to place successfully.");
    }

    @PutMapping("/{planId}/date-window")
    public ResponseEntity<Plan> setDateWindowToPlan(
        @PathVariable Long planId,
        @RequestParam(required = false) Long dateWindowId,
        @RequestBody(required = false) DateWindow newDateWindow,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Plan plan = planService.findById(planId);
        if (plan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (dateWindowId != null) {
            Optional<DateWindow> existingDateWindowOpt = planService.findDateWindowById(dateWindowId);
            if (existingDateWindowOpt.isPresent()) {
                plan.setDateWindow(existingDateWindowOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else if (newDateWindow != null) {
            DateWindow savedDateWindow = planService.saveDateWindow(newDateWindow);
            plan.setDateWindow(savedDateWindow);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Plan updatedPlan = planService.save(plan);
        return ResponseEntity.ok(updatedPlan);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePlan(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @RequestBody Plan updatedPlan) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Long planId = updatedPlan.getId();

        if (!planService.isUserInPlan(planId, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authorized to update this plan.");
        }

        Optional<Plan> existingPlanOpt = Optional.ofNullable(planService.findById(planId));
        if (existingPlanOpt.isPresent()) {
            Plan existingPlan = existingPlanOpt.get();

            existingPlan.setEstCost(updatedPlan.getEstCost());
            existingPlan.setBudget(updatedPlan.getBudget());
            existingPlan.setName(updatedPlan.getName());
            existingPlan.setDateWindow(updatedPlan.getDateWindow());

            DateWindow dateWindow = updatedPlan.getDateWindow();
            if (dateWindow != null) {
                if (dateWindow.getId() == null) {
                    dateWindow = planService.saveDateWindow(dateWindow);
                } else {
                    Optional<DateWindow> existingDateWindow = planService.findDateWindowById(dateWindow.getId());
                    if (!existingDateWindow.isPresent()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DateWindow not found.");
                    }
                }
                existingPlan.setDateWindow(dateWindow);
            }

            List<Place> updatedPlaces = updatedPlan.getPlaces();
            for (Place place : updatedPlaces) {
                DateWindow placeDateWindow = place.getDateWindow();
                if (placeDateWindow != null) {
                    if (placeDateWindow.getId() == null) {
                        // Save new DateWindow
                        placeDateWindow = planService.saveDateWindow(placeDateWindow);
                    } else {
                        // Check if the DateWindow already exists
                        Optional<DateWindow> existingPlaceDateWindow = planService.findDateWindowById(placeDateWindow.getId());
                        if (!existingPlaceDateWindow.isPresent()) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place DateWindow not found.");
                        }
                    }
                    place.setDateWindow(placeDateWindow);
                }
            }

            existingPlan.getPlaces().clear();
            existingPlan.getPlaces().addAll(updatedPlaces);

            Set<String> updatedUsernames = new HashSet<>(updatedPlan.getUsernames());
            existingPlan.setUsernames(updatedUsernames);

            planService.save(existingPlan);

            return ResponseEntity.ok("Plan updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
