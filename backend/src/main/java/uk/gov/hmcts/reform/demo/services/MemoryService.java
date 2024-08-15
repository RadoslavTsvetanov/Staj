package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemoryService {

    @Autowired
    private MemoryRepo memoryRepo;

    public List<Memory> findByLocation(String location) {
        return memoryRepo.findByLocation(location);
    }

    public List<Memory> findByDate(LocalDate date) {
        return memoryRepo.findByDate(date);
    }

    public List<Memory> findByDateAndLocation(LocalDate date, String location) {
        return memoryRepo.findByDateAndLocation(date, location);
    }

    public List<Memory> findByLocationIgnoreCase(String location) {
        return memoryRepo.findByLocationIgnoreCase(location);
    }
}
