package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.History;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.repositories.HistoryRepo;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {

    @Mock
    private HistoryRepo historyRepo;

    @Mock
    private MemoryRepo memoryRepo;

    @InjectMocks
    private HistoryService historyService;

    private History testHistory;
    private Memory testMemory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testHistory = new History();
        testHistory.setId(1L);
        testHistory.setMemories(new HashSet<>());

        testMemory = new Memory();
        testMemory.setId(1L);
        testMemory.setImage("image1");
        testMemory.setDate(LocalDate.of(2024, 9, 1));
        testMemory.setPlace("Place1");
        testMemory.setDescription("Description1");
    }

    @Test
    public void testSave() {
        when(historyRepo.save(testHistory)).thenReturn(testHistory);

        History savedHistory = historyService.save(testHistory);

        assertThat(savedHistory).isNotNull();
        assertThat(savedHistory.getId()).isEqualTo(1L);

        verify(historyRepo, times(1)).save(testHistory);
    }

    @Test
    public void testFindByIdSuccess() {
        when(historyRepo.findById(1L)).thenReturn(Optional.of(testHistory));

        History foundHistory = historyService.findById(1L);

        assertThat(foundHistory).isNotNull();
        assertThat(foundHistory.getId()).isEqualTo(1L);

        verify(historyRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(historyRepo.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            historyService.findById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("History not found with id 1");

        verify(historyRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        when(historyRepo.findAll()).thenReturn(Collections.singletonList(testHistory));

        List<History> historyList = historyService.findAll();

        assertThat(historyList).isNotEmpty();
        assertThat(historyList.size()).isEqualTo(1);
        assertThat(historyList.get(0).getId()).isEqualTo(1L);

        verify(historyRepo, times(1)).findAll();
    }

    @Test
    public void testAddMemoryToHistory() {
        when(historyRepo.findById(1L)).thenReturn(Optional.of(testHistory));
        when(memoryRepo.save(testMemory)).thenReturn(testMemory);
        when(historyRepo.save(testHistory)).thenReturn(testHistory);

        History updatedHistory = historyService.addMemoryToHistory(1L, testMemory);

        assertThat(updatedHistory).isNotNull();
        assertThat(updatedHistory.getMemories()).contains(testMemory);

        verify(historyRepo, times(1)).findById(1L);
        verify(memoryRepo, times(1)).save(testMemory);
        verify(historyRepo, times(1)).save(updatedHistory);
    }
}
