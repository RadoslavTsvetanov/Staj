package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Memory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoryRepo extends JpaRepository<Memory, Long> {
    @Query("SELECT m FROM Memory m WHERE LOWER(m.place) = LOWER(:place) AND m.history.id IN :historyIds")
    List<Memory> findByPlaceAndHistoryIds(@Param("place") String place, @Param("historyIds") List<Long> historyIds);

    @Query("SELECT m FROM Memory m WHERE m.date = :date AND m.history.id IN :historyIds")
    List<Memory> findByDateAndHistoryIds(@Param("date") LocalDate date, @Param("historyIds") List<Long> historyIds);

    @Query("SELECT m FROM Memory m WHERE LOWER(m.place) = LOWER(:place) AND m.date = :date AND m.history.id IN :historyIds")
    List<Memory> findByDateAndPlaceAndHistoryIds(@Param("date") LocalDate date, @Param("place") String place, @Param("historyIds") List<Long> historyIds);

    @Query("SELECT m FROM Memory m WHERE LOWER(m.place) = LOWER(:place)")
    List<Memory> findByPlaceIgnoreCase(@Param("place") String place);
}
