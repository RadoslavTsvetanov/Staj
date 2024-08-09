package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.DateWindow;

public interface DateWindowRepo extends JpaRepository<DateWindow, Long> {
}
