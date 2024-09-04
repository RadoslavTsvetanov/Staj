package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.repositories.DateWindowRepo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DateWindowServiceTest {

    @Mock
    private DateWindowRepo dateWindowRepo;

    @InjectMocks
    private DateWindowService dateWindowService;

    private DateWindow testDateWindow;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testDateWindow = new DateWindow();
        testDateWindow.setId(1L);
        testDateWindow.setStartDate(LocalDate.parse("2024-09-01"));
        testDateWindow.setEndDate(LocalDate.parse("2024-09-30"));
    }

    @Test
    public void testFindAll() {
        when(dateWindowRepo.findAll()).thenReturn(Collections.singletonList(testDateWindow));

        List<DateWindow> dateWindowList = dateWindowService.findAll();

        assertThat(dateWindowList).isNotEmpty();
        assertThat(dateWindowList.size()).isEqualTo(1);
        assertThat(dateWindowList.get(0).getId()).isEqualTo(1L);
        assertThat(dateWindowList.get(0).getStartDate()).isEqualTo("2024-09-01");
        assertThat(dateWindowList.get(0).getEndDate()).isEqualTo("2024-09-30");

        verify(dateWindowRepo, times(1)).findAll();
    }

    @Test
    public void testFindByIdSuccess() {
        when(dateWindowRepo.findById(1L)).thenReturn(Optional.of(testDateWindow));

        DateWindow foundDateWindow = dateWindowService.findById(1L);

        assertThat(foundDateWindow).isNotNull();
        assertThat(foundDateWindow.getId()).isEqualTo(1L);
        assertThat(foundDateWindow.getStartDate()).isEqualTo("2024-09-01");
        assertThat(foundDateWindow.getEndDate()).isEqualTo("2024-09-30");

        verify(dateWindowRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(dateWindowRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            dateWindowService.findById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("DateWindow not found with id: 1");

        verify(dateWindowRepo, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        when(dateWindowRepo.save(testDateWindow)).thenReturn(testDateWindow);

        DateWindow savedDateWindow = dateWindowService.save(testDateWindow);

        assertThat(savedDateWindow).isNotNull();
        assertThat(savedDateWindow.getId()).isEqualTo(1L);
        assertThat(savedDateWindow.getStartDate()).isEqualTo("2024-09-01");
        assertThat(savedDateWindow.getEndDate()).isEqualTo("2024-09-30");

        verify(dateWindowRepo, times(1)).save(testDateWindow);
    }
}
