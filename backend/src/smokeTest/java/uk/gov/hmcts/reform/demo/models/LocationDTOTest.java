package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.demo.models.LocationDTO;

import static org.junit.jupiter.api.Assertions.*;

class LocationDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register JavaTimeModule for Java 8 Date/Time support
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Park");
        locationDTO.setAgeRestriction(true);
        locationDTO.setType("Public");
        locationDTO.setCost(3);
        locationDTO.setDay(15);

        String json = objectMapper.writeValueAsString(locationDTO);

        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Park\""));
        assertTrue(json.contains("\"ageRestriction\":true"));
        assertTrue(json.contains("\"type\":\"Public\""));
        assertTrue(json.contains("\"cost\":3"));
        assertTrue(json.contains("\"day\":15"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"name\": \"Park\", \"ageRestriction\": true, \"type\": \"Public\", \"cost\": 3, \"day\": 15 }";

        LocationDTO locationDTO = objectMapper.readValue(json, LocationDTO.class);

        assertNotNull(locationDTO);
        assertEquals(1L, locationDTO.getId());
        assertEquals("Park", locationDTO.getName());
        assertTrue(locationDTO.getAgeRestriction());
        assertEquals("Public", locationDTO.getType());
        assertEquals(3, locationDTO.getCost());
        assertEquals(15, locationDTO.getDay());
    }

    @Test
    void testDefaultConstructor() {
        LocationDTO locationDTO = new LocationDTO();

        assertNull(locationDTO.getId());
        assertNull(locationDTO.getName());
        assertNull(locationDTO.getAgeRestriction());
        assertNull(locationDTO.getType());
        assertNull(locationDTO.getCost());
        assertNull(locationDTO.getDay());
    }

    @Test
    void testParameterizedConstructor() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Park");
        locationDTO.setAgeRestriction(true);
        locationDTO.setType("Public");
        locationDTO.setCost(3);
        locationDTO.setDay(15);

        assertEquals(1L, locationDTO.getId());
        assertEquals("Park", locationDTO.getName());
        assertTrue(locationDTO.getAgeRestriction());
        assertEquals("Public", locationDTO.getType());
        assertEquals(3, locationDTO.getCost());
        assertEquals(15, locationDTO.getDay());
    }

    @Test
    void testSetters() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Park");
        locationDTO.setAgeRestriction(true);
        locationDTO.setType("Public");
        locationDTO.setCost(3);
        locationDTO.setDay(15);

        assertEquals(1L, locationDTO.getId());
        assertEquals("Park", locationDTO.getName());
        assertTrue(locationDTO.getAgeRestriction());
        assertEquals("Public", locationDTO.getType());
        assertEquals(3, locationDTO.getCost());
        assertEquals(15, locationDTO.getDay());
    }
}
