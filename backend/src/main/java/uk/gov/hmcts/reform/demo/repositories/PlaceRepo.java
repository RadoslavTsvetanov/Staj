package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.demo.models.Place;

import java.util.Optional;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Long> {
    Optional<Place> findByName(String name);
}
