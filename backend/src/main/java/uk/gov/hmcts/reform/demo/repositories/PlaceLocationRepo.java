package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.demo.models.PlaceLocation;

@Repository
public interface PlaceLocationRepo extends JpaRepository<PlaceLocation, Long> {
}
