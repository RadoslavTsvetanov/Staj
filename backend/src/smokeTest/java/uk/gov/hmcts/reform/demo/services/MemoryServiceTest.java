package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MemoryServiceTest {

    @Mock
    private MemoryRepo memoryRepo;

    @Mock
    private PlanRepo planRepo;

    @InjectMocks
    private MemoryService memoryService;

    private Memory testMemory;
    private Plan testPlan;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testMemory = new Memory();
        testMemory.setId(1L);
        testMemory.setPlace("Test Place");
        testMemory.setDate(LocalDate.now());
        testMemory.setDescription("Test Description");

        testPlan = new Plan();
        testPlan.setId(1L);
        testPlan.setHistory(new History());
        testPlan.getHistory().setId(1L);
    }

    @Test
    public void testFindByLocation() {
        when(planRepo.findByUsernamesContaining("testUser")).thenReturn(Collections.singletonList(testPlan));
        when(memoryRepo.findByPlaceAndHistoryIds("Test Place", Collections.singletonList(1L)))
            .thenReturn(Collections.singletonList(testMemory));

        List<Memory> memories = memoryService.findByLocation("testUser", "Test Place");

        assertThat(memories).isNotEmpty();
        assertThat(memories.get(0).getPlace()).isEqualTo("Test Place");

        verify(planRepo, times(1)).findByUsernamesContaining("testUser");
        verify(memoryRepo, times(1)).findByPlaceAndHistoryIds("Test Place", Collections.singletonList(1L));
    }

    @Test
    public void testFindByDate() {
        when(planRepo.findByUsernamesContaining("testUser")).thenReturn(Collections.singletonList(testPlan));
        when(memoryRepo.findByDateAndHistoryIds(LocalDate.now(), Collections.singletonList(1L)))
            .thenReturn(Collections.singletonList(testMemory));

        List<Memory> memories = memoryService.findByDate("testUser", LocalDate.now());

        assertThat(memories).isNotEmpty();
        assertThat(memories.get(0).getDate()).isEqualTo(LocalDate.now());

        verify(planRepo, times(1)).findByUsernamesContaining("testUser");
        verify(memoryRepo, times(1)).findByDateAndHistoryIds(LocalDate.now(), Collections.singletonList(1L));
    }

    @Test
    public void testFindByDateAndPlace() {
        when(planRepo.findByUsernamesContaining("testUser")).thenReturn(Collections.singletonList(testPlan));
        when(memoryRepo.findByDateAndPlaceAndHistoryIds(LocalDate.now(), "Test Place", Collections.singletonList(1L)))
            .thenReturn(Collections.singletonList(testMemory));

        List<Memory> memories = memoryService.findByDateAndPlace("testUser", LocalDate.now(), "Test Place");

        assertThat(memories).isNotEmpty();
        assertThat(memories.get(0).getPlace()).isEqualTo("Test Place");
        assertThat(memories.get(0).getDate()).isEqualTo(LocalDate.now());

        verify(planRepo, times(1)).findByUsernamesContaining("testUser");
        verify(memoryRepo, times(1)).findByDateAndPlaceAndHistoryIds(LocalDate.now(), "Test Place", Collections.singletonList(1L));
    }

    @Test
    public void testFindByPlaceIgnoreCase() {
        when(memoryRepo.findByPlaceIgnoreCase("test place")).thenReturn(Collections.singletonList(testMemory));

        List<Memory> memories = memoryService.findByPlaceIgnoreCase("test place");

        assertThat(memories).isNotEmpty();
        assertThat(memories.get(0).getPlace()).isEqualTo("Test Place");

        verify(memoryRepo, times(1)).findByPlaceIgnoreCase("test place");
    }

    @Test
    public void testSave() {
        when(memoryRepo.save(testMemory)).thenReturn(testMemory);

        Memory savedMemory = memoryService.save(testMemory);

        assertThat(savedMemory).isNotNull();
        assertThat(savedMemory.getId()).isEqualTo(1L);

        verify(memoryRepo, times(1)).save(testMemory);
    }
}
