package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"image\": \"image1\", \"date\": \"2024-09-01\", \"place\": \"Place1\", \"description\": \"Description1\" }";

        MemoryDTO memoryDTO = objectMapper.readValue(json, MemoryDTO.class);

        assertNotNull(memoryDTO);
        assertEquals(1L, memoryDTO.getId(), "ID should be 1");
        assertEquals("image1", memoryDTO.getImage(), "Image should be 'image1'");
        assertEquals(LocalDate.of(2024, 9, 1), memoryDTO.getDate(), "Date should be '2024-09-01'");
        assertEquals("Place1", memoryDTO.getPlace(), "Place should be 'Place1'");
        assertEquals("Description1", memoryDTO.getDescription(), "Description should be 'Description1'");
    }

    @Test
    void testDefaultConstructor() {
        MemoryDTO memoryDTO = new MemoryDTO();

        assertNull(memoryDTO.getId(), "ID should be null");
        assertNull(memoryDTO.getImage(), "Image should be null");
        assertNull(memoryDTO.getDate(), "Date should be null");
        assertNull(memoryDTO.getPlace(), "Place should be null");
        assertNull(memoryDTO.getDescription(), "Description should be null");
    }

    @Test
    void testSetters() {
        MemoryDTO memoryDTO = new MemoryDTO();
        memoryDTO.setId(1L);
        memoryDTO.setImage("image1");
        memoryDTO.setDate(LocalDate.of(2024, 9, 1));
        memoryDTO.setPlace("Place1");
        memoryDTO.setDescription("Description1");

        assertEquals(1L, memoryDTO.getId(), "ID should be 1");
        assertEquals("image1", memoryDTO.getImage(), "Image should be 'image1'");
        assertEquals(LocalDate.of(2024, 9, 1), memoryDTO.getDate(), "Date should be '2024-09-01'");
        assertEquals("Place1", memoryDTO.getPlace(), "Place should be 'Place1'");
        assertEquals("Description1", memoryDTO.getDescription(), "Description should be 'Description1'");
    }
}
