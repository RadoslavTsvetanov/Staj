package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.HistoryRepo;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.services.HistoryService;
import uk.gov.hmcts.reform.demo.services.MemoryService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.EnvThingies;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.client.RestTemplate;


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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HistoryRepo historyRepo;

    @RestController
    @RequestMapping("memory")
    public class MemoryController {
        private static final String UPLOAD_DIR = "uploads";

        @Autowired
        private MemoryRepo memoryRepo;

        @Autowired
        private PlanService planService;

        @Autowired
        private HistoryService historyService;

        @Autowired
        private JwtUtil jwtUtil;

        @PostMapping("/upload")
        public ResponseEntity<String> uploadFile(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("date") @NotBlank String date,
            @RequestParam("place") @NotBlank String place,
            @RequestParam(value = "planId") Long planId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(value = "description", required = false) @Size(max = 500) String description) {

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
            }

            String token = authorizationHeader.substring(7);
            String currentUsername = jwtUtil.getUsernameFromToken(token);

            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
            }

            Optional<Plan> planOptional = Optional.ofNullable(planService.findById(planId));
            if (planOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found.");
            }

            Plan plan = planOptional.get();
            if (!plan.getUsernames().contains(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not part of the plan.");
            }

            boolean isPlaceValid = plan.getPlaces().stream()
                .anyMatch(p -> p.getName().equals(place));

            if (!isPlaceValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Place is not part of the plan.");
            }

            LocalDate memoryDate;
            try {
                memoryDate = LocalDate.parse(date);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid date format.");
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
                memory.setImage(file.getOriginalFilename());
                memory.setDate(memoryDate);
                memory.setPlace(place);
                memory.setDescription(description);

                History history = plan.getHistory();
                if (history == null) {
                    history = new History();
                    plan.setHistory(history);
                    planService.save(plan);
                }

                memory.setHistory(history);
                memoryRepo.save(memory);

                System.out.println(plan.getHistory());

                history.getMemories().add(memory);
                historyService.save(history);

                return ResponseEntity.ok("File uploaded and Memory created successfully!");

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
            }
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<Memory>> searchMemories(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Memory> memories;
        if (location != null && date != null) {
            memories = memoryService.findByDateAndPlace(currentUsername, date, location);
        } else if (location != null) {
            memories = memoryService.findByLocation(currentUsername, location);
        } else if (date != null) {
            memories = memoryService.findByDate(currentUsername, date);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(memories);
    }





}
