package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.DateWindow;

import java.time.LocalDate;

public interface DateWindowRepo extends JpaRepository<DateWindow, Long> {
    DateWindow findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
