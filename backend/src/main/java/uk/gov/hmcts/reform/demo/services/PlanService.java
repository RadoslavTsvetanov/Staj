package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlanService {

    @Autowired
    private PlanRepo planRepo;

    @Autowired
    private UserRepo userRepo;

    public Plan save(Plan plan) {
        return planRepo.save(plan);
    }

    public Plan addUserToPlan(Long planId, String username) {
        Plan plan = planRepo.findById(planId)
            .orElseThrow(() -> new NoSuchElementException("Plan not found with id " + planId));

        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User not found with username " + username);
        }

        plan.getUsers().add(user);
        return planRepo.save(plan);
    }

    public Plan findById(Long id) {
        return planRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Plan not found with id " + id));
    }

    public List<Plan> findAll() {
        return planRepo.findAll();
    }
}
