package uk.gov.hmcts.reform.demo.models;

import java.util.List;

public class PlanDTO {
    private Long id;
    private Integer estCost;
    private Integer budget;
    private String name;
    private List<PlaceDTO> places;
    private HistoryDTO history; // Use HistoryDTO instead of History

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstCost() {
        return estCost;
    }

    public void setEstCost(Integer estCost) {
        this.estCost = estCost;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceDTO> places) {
        this.places = places;
    }

    public HistoryDTO getHistory() {
        return history;
    }

    public void setHistory(HistoryDTO history) {
        this.history = history;
    }
}


