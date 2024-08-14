package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsService credentialsService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credentials credentials = credentialsService.findByEmail(email); //get the user details needed to authenticate the user

        if (credentials == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.builder()
            .username(credentials.getEmail())
            .password(credentials.getPassword())
            .build();
    }
}
