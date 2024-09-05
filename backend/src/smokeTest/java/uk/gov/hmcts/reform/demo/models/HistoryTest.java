package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        History history = new History();
        history.setId(1L);

        Memory memory1 = new Memory();
        memory1.setId(1L);
        memory1.setDescription("Memory 1");

        Memory memory2 = new Memory();
        memory2.setId(2L);
        memory2.setDescription("Memory 2");

        Set<Memory> memories = new HashSet<>();
        memories.add(memory1);
        memories.add(memory2);

        history.setMemories(memories);

        String json = objectMapper.writeValueAsString(history);

        assertNotNull(json);
        assertTrue(json.contains("Memory 1"));
        assertTrue(json.contains("Memory 2"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"memories\": [ { \"id\": 1, \"description\": \"Memory 1\" }, { \"id\": 2, \"description\": \"Memory 2\" } ] }";

        History history = objectMapper.readValue(json, History.class);

        assertNotNull(history);
        assertEquals(1L, history.getId());
        assertEquals(2, history.getMemories().size());
    }

    @Test
    void testAddMemory() {
        History history = new History();

        Memory memory = new Memory();
        memory.setId(1L);
        memory.setDescription("Test Memory");

        Set<Memory> memories = new HashSet<>();
        memories.add(memory);

        history.setMemories(memories);

        assertEquals(1, history.getMemories().size());
        assertTrue(history.getMemories().contains(memory));
    }

    @Test
    void testRemoveMemory() {
        History history = new History();

        Memory memory = new Memory();
        memory.setId(1L);
        memory.setDescription("Test Memory");

        Set<Memory> memories = new HashSet<>();
        memories.add(memory);
        history.setMemories(memories);

        assertEquals(1, history.getMemories().size());

        history.getMemories().remove(memory);

        assertEquals(0, history.getMemories().size());
    }
}
