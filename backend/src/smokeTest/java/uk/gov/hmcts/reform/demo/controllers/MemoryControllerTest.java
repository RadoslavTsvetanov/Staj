package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.services.HistoryService;
import uk.gov.hmcts.reform.demo.services.MemoryService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MemoryControllerTest {

    @InjectMocks
    private MemoryContoller memoryController;

    @Mock
    private MemoryService memoryService;

    @Mock
    private PlanService planService;

    @Mock
    private HistoryService historyService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemoryRepo memoryRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

// TODO: shte se grumna, no joke

//    @Test
//    void uploadFile_Success_ReturnsOk() throws IOException {
//        // Arrange
//        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test content".getBytes());
//        String date = "2024-09-01";
//        String place = "Test Place";
//        Long planId = 1L;
//        String token = "dummy-token";
//        String username = "testuser";
//        String description = "Test Description";
//
//        // Mock the plan and its associated history
//        Plan plan = new Plan();
//        plan.setUsernames(Collections.singleton(username)); // Use Set for usernames
//        History history = new History();
//        plan.setHistory(history);
//
//        // Mock the behavior of dependencies
//        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
//        when(planService.findById(planId)).thenReturn(plan);
//        when(memoryRepo.save(any(Memory.class))).thenReturn(new Memory());
//        when(historyService.save(any(History.class))).thenReturn(history);
//
//        // Act
//        ResponseEntity<String> response = memoryController.uploadFile(file, date, place, planId, "Bearer " + token, description);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("File uploaded and Memory created successfully!", response.getBody());
//
//        // Verify interactions with mocks
//        verify(jwtUtil, times(1)).getUsernameFromToken(token);
//        verify(planService, times(1)).findById(planId);
//        verify(memoryRepo, times(1)).save(any(Memory.class));
//        verify(historyService, times(1)).save(any(History.class));
//    }

    @Test
    void uploadFile_Unauthorized_ReturnsUnauthorized() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test content".getBytes());
        String date = "2024-09-01";
        String place = "Test Place";
        Long planId = 1L;

        ResponseEntity<String> response = memoryController.uploadFile(file, date, place, planId, null, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authorization token is missing or invalid.", response.getBody());
    }

    @Test
    void searchMemories_Success_ReturnsMemories() {
        String token = "dummy-token";
        String username = "testuser";
        LocalDate date = LocalDate.of(2024, 9, 1);
        String location = "Test Location";
        List<Memory> memories = List.of(new Memory());

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(memoryService.findByDateAndPlace(username, date, location)).thenReturn(memories);

        ResponseEntity<List<Memory>> response = memoryController.searchMemories("Bearer " + token, location, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memories, response.getBody());

        verify(jwtUtil, times(1)).getUsernameFromToken(token);
        verify(memoryService, times(1)).findByDateAndPlace(username, date, location);
    }

    @Test
    void searchMemories_Unauthorized_ReturnsUnauthorized() {
        ResponseEntity<List<Memory>> response = memoryController.searchMemories(null, null, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
