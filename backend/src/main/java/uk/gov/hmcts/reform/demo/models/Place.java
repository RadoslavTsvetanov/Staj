package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_seq_gen")
    @SequenceGenerator(name = "place_seq_gen", sequenceName = "place_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @JsonBackReference
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "date_window_id")
    @JsonBackReference("data")
    private DateWindow dateWindow;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference("new")
    private List<PlaceLocation> placeLocations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public DateWindow getDateWindow() {
        return dateWindow;
    }

    public void setDateWindow(DateWindow dateWindow) {
        this.dateWindow = dateWindow;
    }

    public List<PlaceLocation> getPlaceLocations() {
        return placeLocations;
    }

    public void setPlaceLocations(List<PlaceLocation> placeLocations) {
        this.placeLocations = placeLocations;
    }
}
