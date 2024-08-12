package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.History;

public interface HistoryRepo extends JpaRepository<History, Long> {
}
