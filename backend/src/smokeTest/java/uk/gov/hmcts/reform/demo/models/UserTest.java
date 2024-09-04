package uk.gov.hmcts.reform.demo.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setName("Test User");
        user.setProfilePicture("profile.jpg");

        Credentials credentials = new Credentials();
        credentials.setId(1L);
        user.setCredentials(credentials);

        Preferences preferences = new Preferences();
        preferences.setId(1L);
        user.setPreferences(preferences);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals(LocalDate.of(2000, 1, 1), user.getDateOfBirth());
        assertEquals("Test User", user.getName());
        assertEquals("profile.jpg", user.getProfilePicture());
        assertEquals(credentials, user.getCredentials());
        assertEquals(preferences, user.getPreferences());
    }
}

