package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.HistoryRepo;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.services.HistoryService;
import uk.gov.hmcts.reform.demo.services.MemoryService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("memory")
public class MemoryContoller {
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private MemoryRepo memoryRepo;

    @Autowired
    private MemoryService memoryService;

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
        @RequestParam("file") @NotNull MultipartFile file,
        @RequestParam("date") @NotBlank String date,
        @RequestParam("location") @NotBlank String location,
        @RequestParam(value = "planId", required = false) Long planId,
        @RequestParam("username") @NotBlank String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if (!isAdmin) {
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            User user = userOptional.get();

            if (planId == null) {
                return ResponseEntity.badRequest().body("Plan ID is required for non-admin users.");
            }

            Optional<Plan> planOptional = Optional.ofNullable(planService.findById(planId));
            if (planOptional.isEmpty() || !planOptional.get().getUsernames().contains(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not part of the plan.");
            }
            Plan plan = planOptional.get();

            History history = plan.getHistory();
            if (history == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plan does not have an associated history.");
            }
        }

        LocalDate memoryDate;
        try {
            memoryDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format.");
        }

        if (location == null || location.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Location is required.");
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = file.getOriginalFilename();
            assert filename != null;
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            Memory memory = new Memory();
            memory.setImage(filename);
            memory.setDate(memoryDate);
            memory.setLocation(location);

            if (isAdmin) {
                memory.setHistory(null);
            } else {
                Optional<Plan> planOptional = Optional.ofNullable(planService.findById(planId));
                if (planOptional.isPresent()) {
                    History history = planOptional.get().getHistory();
                    if (history != null) {
                        memory.setHistory(history);
                        history.getMemories().add(memory);
                    }
                }
            }

            memoryRepo.save(memory);

            return ResponseEntity.ok("File uploaded and Memory created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @GetMapping("/search")
    public List<Memory> searchMemories(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if(location != null && date != null) {
            return memoryService.findByDateAndLocation(date, location.trim());
        }

        if (location != null) {
            return memoryService.findByLocation(location);
        }

        if (date != null) {
            return memoryService.findByDate(date);
        }
        return new ArrayList<>();
    }
}
