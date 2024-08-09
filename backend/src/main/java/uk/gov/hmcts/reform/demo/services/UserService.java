package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private PreferencesRepo preferencesRepo;

    public User save(User user) {
        if (user.getPreferences() != null && user.getPreferences().getId() == null) {
            // Save preferences only if they are new
            user.getPreferences().setId(null); // Ensure ID is null for new records
            preferencesRepo.save(user.getPreferences());
        }

        if (user.getCredentials() != null && user.getCredentials().getId() == null) {
            // Save credentials only if they are new
            user.getCredentials().setId(null);
            credentialsRepo.save(user.getCredentials());
        }

        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    // New method to save preferences
    public Preferences savePreferences(Preferences preferences) {
        return preferencesRepo.save(preferences);
    }

    public Preferences getPreferencesByUserId(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null ? user.getPreferences() : null;
    }
}
