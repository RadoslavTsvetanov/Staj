package uk.gov.hmcts.reform.demo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.gov.hmcts.reform.demo.models.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        userRepo.save(user);
    }

    @Test
    public void testFindByUsername() {
        User foundUser = userRepo.findByUsername("testuser");
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testFindById() {
        Optional<User> foundUser = userRepo.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testExistsByUsername() {
        assertTrue(userRepo.existsByUsername("testuser"));
    }

    @Test
    public void testFindByUsernameContainingIgnoreCase() {
        List<User> users = userRepo.findByUsernameContainingIgnoreCase("test");
        assertFalse(users.isEmpty());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    public void testDeleteByUsername() {
        userRepo.deleteByUsername("testuser");
        assertFalse(userRepo.existsByUsername("testuser"));
    }
}
