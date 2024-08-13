package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.services.PlanService;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping
    public Plan createPlan(@RequestBody Plan plan) {
        return planService.save(plan);
    }

    @GetMapping
    public List<Plan> getAllPlans() {
        return planService.findAll();
    }

    @GetMapping("/{id}")
    public Plan getPlanById(@PathVariable Long id) {
        return planService.findById(id);
    }

    @PostMapping("/{planId}/users") // taka advame useri, toest http://localhost:4550/plans/{planId}/users?username={username}
    public Plan addUserToPlan(@PathVariable Long planId, @RequestParam String username) {
        return planService.addUserToPlan(planId, username);
    }

    @PutMapping("/{planId}/locations")
    public Plan addLocationsToPlan(@PathVariable Long planId, @RequestBody List<String> locationNames) {
        return planService.addLocationsToPlan(planId, locationNames);
    }

    @PutMapping("/{planId}/date-window")
    public Plan setDateWindowToPlan(
        @PathVariable Long planId,
        @RequestParam(required = false) Long dateWindowId,
        @RequestBody(required = false) DateWindow newDateWindow
    ) {
        return planService.setDateWindowToPlan(planId, dateWindowId, newDateWindow);
    }
}
