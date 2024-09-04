package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        FriendDTO friendDTO = new FriendDTO("john_doe", "profile_pic_url");

        String json = objectMapper.writeValueAsString(friendDTO);

        assertNotNull(json);
        assertTrue(json.contains("john_doe"));
        assertTrue(json.contains("profile_pic_url"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"username\": \"john_doe\", \"profilePicture\": \"profile_pic_url\" }";

        FriendDTO friendDTO = objectMapper.readValue(json, FriendDTO.class);

        assertNotNull(friendDTO);
        assertEquals("john_doe", friendDTO.getUsername());
        assertEquals("profile_pic_url", friendDTO.getProfilePicture());
    }

    @Test
    void testDefaultConstructor() {
        FriendDTO friendDTO = new FriendDTO();

        assertNull(friendDTO.getUsername());
        assertNull(friendDTO.getProfilePicture());
    }

    @Test
    void testParameterizedConstructor() {
        FriendDTO friendDTO = new FriendDTO("john_doe", "profile_pic_url");

        assertEquals("john_doe", friendDTO.getUsername());
        assertEquals("profile_pic_url", friendDTO.getProfilePicture());
    }

    @Test
    void testSetters() {
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setUsername("john_doe");
        friendDTO.setProfilePicture("profile_pic_url");

        assertEquals("john_doe", friendDTO.getUsername());
        assertEquals("profile_pic_url", friendDTO.getProfilePicture());
    }
}
