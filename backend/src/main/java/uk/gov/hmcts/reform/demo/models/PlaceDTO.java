package uk.gov.hmcts.reform.demo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceDTO {
    @NotBlank
    private String name;

    @NotNull
    private Long id;

    @NotNull
    private Long planId;

    @NotNull
    private DateWindowDTO dateWindow;

    private List<PlaceLocationDTO> placeLocations;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull Long getPlanId() {
        return planId;
    }

    public void setPlanId(@NotNull Long planId) {
        this.planId = planId;
    }

    public @NotNull DateWindowDTO getDateWindow() {
        return dateWindow;
    }

    public void setDateWindow(@NotNull DateWindowDTO dateWindow) {
        this.dateWindow = dateWindow;
    }

    public List<PlaceLocationDTO> getPlaceLocations() {
        return placeLocations;
    }

    public void setPlaceLocations(List<PlaceLocationDTO> placeLocations) {
        this.placeLocations = placeLocations;
    }

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }
}
