package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"memories\": [ { \"id\": 1, \"image\": \"image1\", \"date\": \"2024-09-01\", \"place\": \"Place1\", \"description\": \"Description1\" }, { \"id\": 2, \"image\": \"image2\", \"date\": \"2024-09-02\", \"place\": \"Place2\", \"description\": \"Description2\" } ] }";

        HistoryDTO historyDTO = objectMapper.readValue(json, HistoryDTO.class);

        assertNotNull(historyDTO);
        assertEquals(1L, historyDTO.getId());
        assertNotNull(historyDTO.getMemories());
        assertEquals(2, historyDTO.getMemories().size());

        MemoryDTO memoryDTO1 = historyDTO.getMemories().get(0);
        assertEquals(1L, memoryDTO1.getId());
        assertEquals("image1", memoryDTO1.getImage());
        assertEquals(LocalDate.of(2024, 9, 1), memoryDTO1.getDate());
        assertEquals("Place1", memoryDTO1.getPlace());
        assertEquals("Description1", memoryDTO1.getDescription());

        MemoryDTO memoryDTO2 = historyDTO.getMemories().get(1);
        assertEquals(2L, memoryDTO2.getId());
        assertEquals("image2", memoryDTO2.getImage());
        assertEquals(LocalDate.of(2024, 9, 2), memoryDTO2.getDate());
        assertEquals("Place2", memoryDTO2.getPlace());
        assertEquals("Description2", memoryDTO2.getDescription());
    }

    @Test
    void testDefaultConstructor() {
        HistoryDTO historyDTO = new HistoryDTO();

        assertNull(historyDTO.getId());
        assertNull(historyDTO.getMemories());
    }

    @Test
    void testParameterizedConstructor() {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(1L);
        historyDTO.setMemories(new ArrayList<>());

        assertEquals(1L, historyDTO.getId());
        assertNotNull(historyDTO.getMemories());
        assertTrue(historyDTO.getMemories().isEmpty());
    }
}
