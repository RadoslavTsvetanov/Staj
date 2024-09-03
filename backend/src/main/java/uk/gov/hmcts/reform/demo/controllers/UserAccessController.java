package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.services.EntityToDtoMapper;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-access")
public class UserAccessController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService auth;

    @Autowired
    private PreferencesRepo preferencesRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    private static final String UPLOAD_DIR = "uploads/profile_pictures";

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDTO>> getUserFriends(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<User> friends = user.getFriends();

            List<FriendDTO> friendDTOs = friends.stream()
                .map(friend -> new FriendDTO(friend.getUsername(), friend.getProfilePicture()))
                .collect(Collectors.toList());

            return ResponseEntity.ok(friendDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlanDTO>> getUserPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build();
        }

        List<Plan> plans = planService.findPlansByUsername(username);
        List<PlanDTO> planDTOs = plans.stream()
            .map(EntityToDtoMapper::toPlanDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(planDTOs);
    }

    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @RequestParam("friendUsername") String friendUsername) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Optional<User> currentUserOpt = userService.findByUsername(currentUsername);
        Optional<User> friendUserOpt = userService.findByUsername(friendUsername);

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user not found.");
        }

        if (!friendUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend user not found.");
        }

        User currentUser = currentUserOpt.get();
        User friendUser = friendUserOpt.get();

        if (currentUser.getFriends().contains(friendUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already friends.");
        }

        currentUser.getFriends().add(friendUser);
        userService.save(currentUser);

        friendUser.getFriends().add(currentUser);
        userService.save(friendUser);

        return ResponseEntity.ok("Friend added successfully.");
    }


    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getProfilePicture() != null) {
                String profilePictureUrl = "/uploads/profile_pictures/" + user.getProfilePicture();
                user.setProfilePicture(profilePictureUrl);
            }

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @RequestBody User updatedUser) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> existingUserOpt = userService.findByUsername(currentUsername);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (!existingUser.getCredentials().getEmail().equals(updatedUser.getCredentials().getEmail())) {
                boolean emailExists = userService.checkIfEmailExists(updatedUser.getCredentials().getEmail());
                if (emailExists) {
                    return ResponseEntity.status(409).body("Email is already registered.");
                }
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

            if (updatedUser.getPreferences() != null) {
                Preferences updatedPreferences = updatedUser.getPreferences();
                if (updatedPreferences.getId() == null) {
                    preferencesRepo.save(updatedPreferences);
                    existingUser.setPreferences(updatedPreferences);
                } else {
                    preferencesRepo.save(updatedPreferences);
                    existingUser.setPreferences(updatedPreferences);
                }
            }

            Credentials updatedCredentials = updatedUser.getCredentials();
            if (updatedCredentials.getId() != null) {
                Credentials existingCredentials = credentialsRepo.findById(updatedCredentials.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Credentials not found"));

                if (!existingCredentials.getEmail().equals(updatedCredentials.getEmail())) {
                    boolean emailExists = userService.checkIfEmailExists(updatedCredentials.getEmail());
                    if (emailExists) {
                        return ResponseEntity.status(409).body("Email is already registered.");
                    }
                }

                existingCredentials.setEmail(updatedCredentials.getEmail());
                existingCredentials.setPassword(updatedCredentials.getPassword());

                credentialsRepo.save(existingCredentials);
                existingUser.setCredentials(existingCredentials);
            }

            User savedUser = userService.save(existingUser);
            String newToken = auth.issueToken(savedUser.getUsername());

            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newToken)
                .body(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profile/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @RequestParam("file") MultipartFile file) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Optional<User> existingUserOpt = userService.findByUsername(username);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            try {
                String response = Utils.uploadFile(file.getBytes(), file.getOriginalFilename(), username);

                existingUser.setProfilePicture(file.getOriginalFilename());
                userService.save(existingUser);

                return ResponseEntity.ok("Profile picture uploaded successfully! Server response: " + response);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
