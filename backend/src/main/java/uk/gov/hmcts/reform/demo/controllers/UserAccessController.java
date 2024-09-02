package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.services.EntityToDtoMapper;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.EnvThingies;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @GetMapping("/plans")
    public ResponseEntity<List<PlanDTO>> getUserPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<Plan> plans = planService.findPlansByUsername(username);
        List<PlanDTO> planDTOs = plans.stream()
            .map(EntityToDtoMapper::toPlanDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(planDTOs);
    }

    private PlanDTO toPlanDTO(Plan plan) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setEstCost(plan.getEstCost());
        planDTO.setBudget(plan.getBudget());
        planDTO.setName(plan.getName());
        planDTO.setPlaces(plan.getPlaces().stream()
                              .map(EntityToDtoMapper::toPlaceDTO)
                              .collect(Collectors.toList()));
        return planDTO;
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
                    // New preferences, save them
                    preferencesRepo.save(updatedPreferences);
                    existingUser.setPreferences(updatedPreferences);
                } else {
                    // Existing preferences, update them
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

        System.out.println("AAAA");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        System.out.println("username");

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Optional<User> existingUserOpt = userService.findByUsername(username);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String oldFilename = existingUser.getProfilePicture();
                if (oldFilename != null) {
                    Path oldFilePath = uploadPath.resolve(oldFilename);
                    Files.deleteIfExists(oldFilePath);
                }

                String filename = username + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.write(filePath, file.getBytes());

                String picUrl = Utils.uploadFile(file.getBytes(),"","staj");

                existingUser.setProfilePicture(picUrl);
                userService.save(existingUser);


                return ResponseEntity.ok("Profile picture uploaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }


}
