package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Memory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoryRepo extends JpaRepository<Memory, Long> {
    List<Memory> findByLocation(String location);
    List<Memory> findByDate(LocalDate date);
    List<Memory> findByDateAndLocation(LocalDate date, String location);


    @Query("SELECT m FROM Memory m WHERE LOWER(m.location) = LOWER(:location)")
    List<Memory> findByLocationIgnoreCase(@Param("location") String location);
}
