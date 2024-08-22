package uk.gov.hmcts.reform.demo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.hmcts.reform.demo.models.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanRepo extends JpaRepository<Plan, Long> {
    Optional<Plan> findByName(String name);

//    @Query("SELECT p FROM Plan p WHERE :username MEMBER OF p.usernames")
//    List<Plan> findPlansByUsername(@Param("username") String username);

    List<Plan> findAllByUsernamesContaining(String username);

    @Query("SELECT p FROM Plan p WHERE :username MEMBER OF p.usernames")
    List<Plan> findPlansByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {"places.placeLocations"})
    List<Plan> findAll();
}
