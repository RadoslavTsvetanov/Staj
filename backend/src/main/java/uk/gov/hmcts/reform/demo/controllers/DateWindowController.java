package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.services.DateWindowService;
import java.util.List;

@RestController
@RequestMapping("/date-window")
public class DateWindowController {

    @Autowired
    private DateWindowService dateWindowService;

    @GetMapping
    public ResponseEntity<List<DateWindow>> getAllDateWindows() {
        List<DateWindow> dateWindows = dateWindowService.findAll();
        return ResponseEntity.ok(dateWindows);
    }

    @PostMapping
    public ResponseEntity<DateWindow> createDateWindow(@Valid @RequestBody DateWindow dateWindow) {
        DateWindow savedDateWindow = dateWindowService.save(dateWindow);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDateWindow);
    }
}
