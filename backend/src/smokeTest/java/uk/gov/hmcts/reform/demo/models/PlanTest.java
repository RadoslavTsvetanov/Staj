package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    private ObjectMapper objectMapper;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setName("Test Plan");
        plan.setEstCost(3);
        plan.setBudget(2);
        plan.addUsername("user1");
        plan.addUsername("user2");

        String json = objectMapper.writeValueAsString(plan);

        assertNotNull(json);
        assertTrue(json.contains("Test Plan"));
        assertTrue(json.contains("3"));
        assertTrue(json.contains("user1"));
        assertTrue(json.contains("user2"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"name\": \"Test Plan\", \"estCost\": 3, \"budget\": 2, \"usernames\": [\"user1\", \"user2\"] }";

        Plan plan = objectMapper.readValue(json, Plan.class);

        assertNotNull(plan);
        assertEquals(1L, plan.getId());
        assertEquals("Test Plan", plan.getName());
        assertEquals(3, plan.getEstCost());
        assertEquals(2, plan.getBudget());
        assertTrue(plan.getUsernames().contains("user1"));
        assertTrue(plan.getUsernames().contains("user2"));
    }

    @Test
    void testValidationSuccess() {
        Plan plan = new Plan();
        plan.setEstCost(2);
        plan.setBudget(3);
        plan.setName("Valid Plan");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(plan, "plan");
        validator.validate(plan, errors);

        assertFalse(errors.hasErrors(), "Plan should pass validation");
    }

    @Test
    void testValidationFailure_EstCostBelowMin() {
        Plan plan = new Plan();
        plan.setEstCost(-1); // Invalid cost, below minimum
        plan.setBudget(3);
        plan.setName("Invalid Plan");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(plan, "plan");
        validator.validate(plan, errors);

        assertTrue(errors.hasFieldErrors("estCost"), "EstCost below minimum should fail validation");
    }

    @Test
    void testValidationFailure_EstCostAboveMax() {
        Plan plan = new Plan();
        plan.setEstCost(5); // Invalid cost, above maximum
        plan.setBudget(3);
        plan.setName("Invalid Plan");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(plan, "plan");
        validator.validate(plan, errors);

        assertTrue(errors.hasFieldErrors("estCost"), "EstCost above maximum should fail validation");
    }

    @Test
    void testValidationFailure_BudgetBelowMin() {
        Plan plan = new Plan();
        plan.setEstCost(2);
        plan.setBudget(-1); // Invalid budget, below minimum
        plan.setName("Invalid Plan");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(plan, "plan");
        validator.validate(plan, errors);

        assertTrue(errors.hasFieldErrors("budget"), "Budget below minimum should fail validation");
    }

    @Test
    void testValidationFailure_BudgetAboveMax() {
        Plan plan = new Plan();
        plan.setEstCost(2);
        plan.setBudget(5); // Invalid budget, above maximum
        plan.setName("Invalid Plan");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(plan, "plan");
        validator.validate(plan, errors);

        assertTrue(errors.hasFieldErrors("budget"), "Budget above maximum should fail validation");
    }

    @Test
    void testAddUsername() {
        Plan plan = new Plan();
        plan.addUsername("user1");
        plan.addUsername("user2");

        assertTrue(plan.getUsernames().contains("user1"));
        assertTrue(plan.getUsernames().contains("user2"));
    }

    @Test
    void testRemoveUsername() {
        Plan plan = new Plan();
        plan.addUsername("user1");
        plan.addUsername("user2");
        plan.removeUsername("user1");

        assertFalse(plan.getUsernames().contains("user1"));
        assertTrue(plan.getUsernames().contains("user2"));
    }

    @Test
    void testSettingAndGettingPlaces() {
        Plan plan = new Plan();
        Place place = new Place();
        place.setName("Place1");
        plan.setPlaces(List.of(place));

        assertEquals(1, plan.getPlaces().size());
        assertEquals("Place1", plan.getPlaces().get(0).getName());
    }
}
