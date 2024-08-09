package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    public User save(User user) {
        // First, save the credentials if they are not null
        if (user.getCredentials() != null) {
            Credentials credentials = user.getCredentials();
            if (credentials.getId() == null) { // Check if the credentials are new
                credentials = credentialsRepo.save(credentials);
                user.setCredentials(credentials);
            }
        }
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
}

