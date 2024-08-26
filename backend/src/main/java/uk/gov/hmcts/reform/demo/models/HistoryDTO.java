package uk.gov.hmcts.reform.demo.models;

import java.util.List;

public class HistoryDTO {
    private Long id;
    private List<MemoryDTO> memories;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MemoryDTO> getMemories() {
        return memories;
    }

    public void setMemories(List<MemoryDTO> memories) {
        this.memories = memories;
    }
}

