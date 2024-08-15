package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.services.DateWindowService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/date-windows")
public class DateWindowController {

    @Autowired
    private DateWindowService dateWindowService;

    @GetMapping
    public List<DateWindow> getAllDateWindows() {
        return dateWindowService.findAll();
    }

    @PostMapping
    public DateWindow createDateWindow(@RequestBody DateWindow dateWindow) {
        return dateWindowService.save(dateWindow);
    }

    @GetMapping("/{id}")
    public Optional<DateWindow> getDateWindowById(@PathVariable Long id) {
        return dateWindowService.findById(id);
    }
}
