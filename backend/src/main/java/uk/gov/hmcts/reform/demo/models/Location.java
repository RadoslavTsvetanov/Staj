package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq_gen")
    @SequenceGenerator(name = "location_seq_gen", sequenceName = "location_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name should not exceed 255 characters")
    private String name;

    @NotNull(message = "Age restriction cannot be null")
    private Boolean ageRestriction;

    @NotBlank(message = "Type cannot be empty")
    @Size(max = 50, message = "Type should not exceed 50 characters")
    private String type;

    @Min(value = 0, message = "Cost must be between 0 and 4")
    @Max(value = 4, message = "Cost must be between 0 and 4")
    private Integer cost;

    @Min(value = 0, message = "Cost must be between 1 and 28")
    @Max(value = 28, message = "Cost must be between 1 and 28")
    private Integer day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Boolean ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public @Min(value = 0, message = "Cost must be between 1 and 28") @Max(value = 28, message = "Cost must be between 1 and 28") Integer getDay() {
        return day;
    }

    public void setDay(@Min(value = 0, message = "Cost must be between 1 and 28") @Max(value = 28, message = "Cost must be between 1 and 28") Integer day) {
        this.day = day;
    }
}
