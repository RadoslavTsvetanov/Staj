package uk.gov.hmcts.reform.demo.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    void deserialize_ValidDateString_ReturnsLocalDate() throws JsonProcessingException {
        String jsonDate = "\"2024-09-01\"";
        LocalDate expectedDate = LocalDate.of(2024, 9, 1);

        LocalDate actualDate = objectMapper.readValue(jsonDate, LocalDate.class);

        assertEquals(expectedDate, actualDate);
    }
}
