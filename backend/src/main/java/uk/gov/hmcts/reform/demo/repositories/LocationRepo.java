package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Location;

import java.util.Optional;

public interface LocationRepo extends JpaRepository<Location, Long> {
    Location findByName(String name);
}
