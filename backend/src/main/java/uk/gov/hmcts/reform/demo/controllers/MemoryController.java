package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.services.MemoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private MemoryRepo memoryRepo;

    @Autowired
    private MemoryService memoryService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("date") String date,
        @RequestParam("location") String location) {

        // Validate the file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        // Validate the date
        LocalDate memoryDate;
        try {
            memoryDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format.");
        }

        // Validate the location
        if (location == null || location.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Location is required.");
        }

        try {
            // Create upload directory if it does not exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            String filename = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            // Create a new Memory entity and save it to the database
            Memory memory = new Memory();
            memory.setImage(filename);
            memory.setDate(memoryDate);
            memory.setLocation(location);
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
        } else if (location != null) {
            return memoryService.findByLocationIgnoreCase(location.trim()); // ne raboti poradi nqkakva prichina, koqto az ne moga da razbera
        } else if (date != null) {
            return memoryService.findByDate(date);
        } else {
            return new ArrayList<>();
        }
    }
}

