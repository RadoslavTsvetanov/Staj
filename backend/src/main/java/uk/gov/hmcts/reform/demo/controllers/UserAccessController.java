package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.services.HistoryService;
import uk.gov.hmcts.reform.demo.services.MemoryService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-access")
public class UserAccessController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MemoryService memoryService;

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getUserPlans(Principal principal) {
        String username = principal.getName();
        List<Plan> plans = planService.findPlansByUsername(username);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/preferences")
    public ResponseEntity<User> getUserPreferences(Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);

        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
