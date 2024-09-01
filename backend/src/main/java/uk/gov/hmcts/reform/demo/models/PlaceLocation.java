package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "place_location")
public class PlaceLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_location_seq_gen")
    @SequenceGenerator(name = "place_location_seq_gen", sequenceName = "place_location_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @JsonBackReference("new")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @JsonBackReference
    private Location location;

    @Min(1)
    @Max(28)
    private Integer day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public @Min(1) @Max(28) Integer getDay() {
        return day;
    }

    public void setDay(@Min(1) @Max(28) Integer day) {
        this.day = day;
    }
}

