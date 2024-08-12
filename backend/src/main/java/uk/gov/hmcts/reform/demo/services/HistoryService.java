package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.repositories.HistoryRepo;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private MemoryRepo memoryRepo;

    public History save(History history) {
        return historyRepo.save(history);
    }

    public History findById(Long id) {
        return historyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("History not found with id " + id));
    }

    public List<History> findAll() {
        return historyRepo.findAll();
    }

    public History addMemoryToHistory(Long historyId, Memory memory) {
        History history = findById(historyId);
        memory.setHistory(history);
        memoryRepo.save(memory);
        history.getMemories().add(memory);
        return historyRepo.save(history);
    }
}
