package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.services.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        System.out.println("Received login request with email: " + email);
        return authenticationService.authenticate(email, password);
    }

    //@PostMapping("/login")
    //public String login(@RequestBody Map<String, String> loginRequest) {
    //    String email = loginRequest.get("email");
    //    String password = loginRequest.get("password");
    //    return authenticationService.authenticate(email, password);
    //}

    @PostMapping("/logout")
    public void logout(@RequestParam String token) {
        authenticationService.logout(token);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
        try {
            authenticationService.validateToken(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
