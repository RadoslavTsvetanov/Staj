package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "memories")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memory_seq_gen")
    @SequenceGenerator(name = "memory_seq_gen", sequenceName = "memory_seq", allocationSize = 1)
    private Long id;

    private String image;
    private LocalDate date;
    private String location;

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
}
