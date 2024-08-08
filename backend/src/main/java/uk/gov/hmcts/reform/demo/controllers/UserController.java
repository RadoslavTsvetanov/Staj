package uk.gov.hmcts.reform.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private uk.gov.hmcts.reform.demo.services.UserService testTableService;

    @GetMapping
    public List<User> getAllTestTables() {
        return testTableService.findAll();
    }

    @PostMapping
    public User createTestTable(@RequestBody User testTable) {
        return testTableService.save(testTable);
    }
}
