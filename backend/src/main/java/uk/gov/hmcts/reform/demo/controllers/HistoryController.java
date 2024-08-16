package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.services.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping
    public ResponseEntity<History> createHistory(@RequestBody History history) {
        History createdHistory = historyService.save(history);
        return new ResponseEntity<>(createdHistory, HttpStatus.CREATED);
    }

    @GetMapping
    public List<History> getAllHistories() {
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<History> getHistoryById(@PathVariable Long id) {
        History history = historyService.findById(id);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{historyId}/memories")
    public ResponseEntity<History> addMemoryToHistory(
        @PathVariable Long historyId,
        @RequestBody Memory memory) {

        History updatedHistory = historyService.addMemoryToHistory(historyId, memory);
        return ResponseEntity.ok(updatedHistory);
    }
}
