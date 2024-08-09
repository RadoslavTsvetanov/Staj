package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Preferences;

public interface PreferencesRepo extends JpaRepository<Preferences, Long> {
}
