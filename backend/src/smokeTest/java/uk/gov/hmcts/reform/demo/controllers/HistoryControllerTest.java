package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.services.HistoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class HistoryControllerTest {

    @InjectMocks
    private HistoryController historyController;

    @Mock
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createHistory_Success_ReturnsCreatedHistory() {
        History history = new History();
        History createdHistory = new History();
        createdHistory.setId(1L); // Assuming ID is generated

        when(historyService.save(any(History.class))).thenReturn(createdHistory);

        ResponseEntity<History> response = historyController.createHistory(history);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdHistory, response.getBody());
        verify(historyService, times(1)).save(any(History.class));
    }

    @Test
    void getAllHistories_Success_ReturnsHistories() {
        History history1 = new History();
        History history2 = new History();
        List<History> histories = Arrays.asList(history1, history2);

        when(historyService.findAll()).thenReturn(histories);

        List<History> response = historyController.getAllHistories();

        assertEquals(histories, response);
        verify(historyService, times(1)).findAll();
    }

    @Test
    void getHistoryById_Success_ReturnsHistory() {
        History history = new History();
        Long id = 1L;

        when(historyService.findById(anyLong())).thenReturn(history);

        ResponseEntity<History> response = historyController.getHistoryById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(history, response.getBody());
        verify(historyService, times(1)).findById(anyLong());
    }

    @Test
    void addMemoryToHistory_Success_ReturnsUpdatedHistory() {
        Memory memory = new Memory();
        History updatedHistory = new History();
        Long historyId = 1L;

        when(historyService.addMemoryToHistory(anyLong(), any(Memory.class))).thenReturn(updatedHistory);

        ResponseEntity<History> response = historyController.addMemoryToHistory(historyId, memory);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedHistory, response.getBody());
        verify(historyService, times(1)).addMemoryToHistory(anyLong(), any(Memory.class));
    }
}
