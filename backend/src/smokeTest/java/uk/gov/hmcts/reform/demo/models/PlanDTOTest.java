package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlanDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register JavaTimeModule for Java 8 Date/Time support
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(1L);
        planDTO.setEstCost(2);
        planDTO.setBudget(3);
        planDTO.setName("Plan1");
        planDTO.setDateWindow(new DateWindowDTO(LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30)));
        planDTO.setUsernames(Collections.singletonList("user1"));
        planDTO.setPlaces(Collections.singletonList(
            new PlaceDTO(1L, "Place1", 2L, new DateWindowDTO(LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30)), Collections.emptyList())
        ));
        planDTO.setHistory(new HistoryDTO(1L, Collections.singletonList(new MemoryDTO(1L, "image1", LocalDate.of(2024, 9, 1), "Place1", "Description1"))));

        String json = objectMapper.writeValueAsString(planDTO);

        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"), "JSON should contain id");
        assertTrue(json.contains("\"estCost\":2"), "JSON should contain estCost");
        assertTrue(json.contains("\"budget\":3"), "JSON should contain budget");
        assertTrue(json.contains("\"name\":\"Plan1\""), "JSON should contain name");
        assertTrue(json.contains("\"dateWindow\""), "JSON should contain dateWindow");
        assertTrue(json.contains("\"usernames\""), "JSON should contain usernames");
        assertTrue(json.contains("\"places\""), "JSON should contain places");
        assertTrue(json.contains("\"history\""), "JSON should contain history");
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"estCost\": 2, \"budget\": 3, \"name\": \"Plan1\", \"dateWindow\": { \"startDate\": \"2024-09-01\", \"endDate\": \"2024-09-30\" }, \"usernames\": [\"user1\"], \"places\": [{ \"id\": 1, \"name\": \"Place1\", \"planId\": 2, \"dateWindow\": { \"startDate\": \"2024-09-01\", \"endDate\": \"2024-09-30\" }, \"placeLocations\": [] }], \"history\": { \"id\": 1, \"memories\": [{ \"id\": 1, \"image\": \"image1\", \"date\": \"2024-09-01\", \"place\": \"Place1\", \"description\": \"Description1\" }] } }";

        PlanDTO planDTO = objectMapper.readValue(json, PlanDTO.class);

        assertNotNull(planDTO);
        assertEquals(1L, planDTO.getId(), "ID should be 1");
        assertEquals(2, planDTO.getEstCost(), "EstCost should be 2");
        assertEquals(3, planDTO.getBudget(), "Budget should be 3");
        assertEquals("Plan1", planDTO.getName(), "Name should be 'Plan1'");
        assertNotNull(planDTO.getDateWindow(), "DateWindow should not be null");
        assertEquals(LocalDate.of(2024, 9, 1), planDTO.getDateWindow().getStartDate(), "StartDate should be '2024-09-01'");
        assertEquals(LocalDate.of(2024, 9, 30), planDTO.getDateWindow().getEndDate(), "EndDate should be '2024-09-30'");
        assertNotNull(planDTO.getUsernames(), "Usernames should not be null");
        assertEquals(1, planDTO.getUsernames().size(), "Usernames size should be 1");
        assertEquals("user1", planDTO.getUsernames().get(0), "Usernames should contain 'user1'");
        assertNotNull(planDTO.getPlaces(), "Places should not be null");
        assertEquals(1, planDTO.getPlaces().size(), "Places size should be 1");
        assertEquals(1L, planDTO.getPlaces().get(0).getId(), "Place ID should be 1");
        assertNotNull(planDTO.getHistory(), "History should not be null");
        assertNotNull(planDTO.getHistory().getMemories(), "History memories should not be null");
        assertEquals(1, planDTO.getHistory().getMemories().size(), "History memories size should be 1");
        assertEquals("image1", planDTO.getHistory().getMemories().get(0).getImage(), "Memory image should be 'image1'");
    }

    @Test
    void testDefaultConstructor() {
        PlanDTO planDTO = new PlanDTO();

        assertNull(planDTO.getId(), "ID should be null");
        assertNull(planDTO.getEstCost(), "EstCost should be null");
        assertNull(planDTO.getBudget(), "Budget should be null");
        assertNull(planDTO.getName(), "Name should be null");
        assertNull(planDTO.getDateWindow(), "DateWindow should be null");
        assertNull(planDTO.getUsernames(), "Usernames should be null");
        assertNull(planDTO.getPlaces(), "Places should be null");
        assertNull(planDTO.getHistory(), "History should be null");
    }
}
