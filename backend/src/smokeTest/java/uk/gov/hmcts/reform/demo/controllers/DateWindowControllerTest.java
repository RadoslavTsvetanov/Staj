package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.services.DateWindowService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DateWindowControllerTest {

    @InjectMocks
    private DateWindowController dateWindowController;

    @Mock
    private DateWindowService dateWindowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDateWindows_Success_ReturnsDateWindows() {
        DateWindow dateWindow1 = new DateWindow();
        DateWindow dateWindow2 = new DateWindow();
        List<DateWindow> dateWindows = Arrays.asList(dateWindow1, dateWindow2);

        when(dateWindowService.findAll()).thenReturn(dateWindows);

        ResponseEntity<List<DateWindow>> response = dateWindowController.getAllDateWindows();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dateWindows, response.getBody());
        verify(dateWindowService, times(1)).findAll();
    }

    @Test
    void createDateWindow_Success_ReturnsCreatedDateWindow() {
        DateWindow dateWindow = new DateWindow();
        DateWindow savedDateWindow = new DateWindow();
        savedDateWindow.setId(1L); // Assuming ID is generated

        when(dateWindowService.save(any(DateWindow.class))).thenReturn(savedDateWindow);

        ResponseEntity<DateWindow> response = dateWindowController.createDateWindow(dateWindow);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedDateWindow, response.getBody());
        verify(dateWindowService, times(1)).save(any(DateWindow.class));
    }
}
