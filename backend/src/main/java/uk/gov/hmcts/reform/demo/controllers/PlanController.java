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
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build();
        }

        if (planService.findByName(plan.getName()).isPresent()) {
            return ResponseEntity.status(409).build();
        }

        plan.addUsername(username);
        Plan savedPlan = planService.save(plan);
        return ResponseEntity.status(201).body(savedPlan);
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
        @RequestBody List<String> placeNames,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Plan updatedPlan = planService.addPlacesToPlan(planId, placeNames);
        return ResponseEntity.ok("Places added successfully.");
    }

    @PutMapping("/{planId}/places/{placeId}/locations")
    public ResponseEntity<String> addLocationsToPlace(
        @PathVariable Long planId,
        @PathVariable Long placeId,
        @RequestBody List<String> locationNames,
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        for (String locationName : locationNames) {
            Optional<Location> location = Optional.ofNullable(locationService.findByName(locationName));
            if (location.isEmpty()) {
                return ResponseEntity.status(404).body("Location not found: " + locationName);
            }
        }

        Plan updatedPlan = planService.addLocationsToPlace(planId, placeId, locationNames);
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
}
