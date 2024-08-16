package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateUsernameException;
import uk.gov.hmcts.reform.demo.exceptions.UsernameNotFoundException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private PreferencesRepo preferencesRepo;

    @Autowired
    private PlanService planService;

    public User createUser(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException("Username " + user.getUsername() + " already exists");
        }

        if (user.getCredentials() != null && user.getCredentials().getId() != null) {
            Optional<Credentials> optionalCredentials = credentialsRepo.findById(user.getCredentials().getId());
            if (optionalCredentials.isPresent()) {
                user.setCredentials(optionalCredentials.get());
            } else {
                throw new RuntimeException("Credentials with ID " + user.getCredentials().getId() + " not found.");
            }
        }

        if (user.getPreferences() != null && user.getPreferences().getId() != null) {
            Optional<Preferences> optionalPreferences = preferencesRepo.findById(user.getPreferences().getId());
            if (optionalPreferences.isPresent()) {
                user.setPreferences(optionalPreferences.get());
            } else {
                throw new RuntimeException("Preferences with ID " + user.getPreferences().getId() + " not found.");
            }
        }
        return userRepo.save(user);
    }

    public User addCredentialsAndPreferences(Long userId, Credentials credentials, Preferences preferences) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (credentials.getId() == null) {
            if (credentialsRepo.existsByEmail(credentials.getEmail())) {
                throw new DuplicateEmailException("Email " + credentials.getEmail() + " already exists");
            }
            credentials = credentialsRepo.save(credentials);
        }
        if (preferences.getId() == null) {
            preferences = preferencesRepo.save(preferences);
        }

        user.setCredentials(credentials);
        user.setPreferences(preferences);
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(Optional.ofNullable(userRepo.findByUsername(username))
                                       .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found")));
    }

    public Preferences savePreferences(Preferences preferences) {
        return preferencesRepo.save(preferences);
    }

    public Preferences getPreferencesByUserId(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null ? user.getPreferences() : null;
    }

    public List<User> searchByUsername(String username) {
        return userRepo.findByUsernameContainingIgnoreCase(username);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public void deleteUser(Long id) {
        planService.removeUserFromAllPlans(id);
        userRepo.deleteById(id);
    }
}
