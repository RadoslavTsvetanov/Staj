package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.demo.validators.ValidDateWindow;

import java.time.LocalDate;

@Entity
@Table(name = "date_window")
@ValidDateWindow
public class DateWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "date_window_seq_gen")
    @SequenceGenerator(name = "date_window_seq_gen", sequenceName = "date_window_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
