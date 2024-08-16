package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "memories")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memory_seq_gen")
    @SequenceGenerator(name = "memory_seq_gen", sequenceName = "memory_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Image cannot be blank")
    private String image;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @ManyToOne
    @JoinColumn(name = "history_id")
    @JsonBackReference
    private History history;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
