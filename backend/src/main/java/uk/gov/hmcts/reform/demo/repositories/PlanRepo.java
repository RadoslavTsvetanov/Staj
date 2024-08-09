package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.demo.models.Plan;

public interface PlanRepo extends JpaRepository<Plan, Long> {
}
