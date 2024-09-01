package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    public @NotBlank(message = "Place cannot be blank") String getPlace() {
        return place;
    }

    public void setPlace(@NotBlank(message = "Place cannot be blank") String place) {
        this.place = place;
    }

    @NotBlank(message = "Place cannot be blank")
    private String place;

    @ManyToOne
    @JoinColumn(name = "history_id")
    @JsonBackReference
    private History history;

    @Size(max = 500, message = "Description can't be longer than 500 characters")
    private String description;

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

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
