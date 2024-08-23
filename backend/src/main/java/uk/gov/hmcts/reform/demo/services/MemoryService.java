package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Memory;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.MemoryRepo;
import uk.gov.hmcts.reform.demo.repositories.PlanRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryService {

    @Autowired
    private MemoryRepo memoryRepo;

    @Autowired
    private PlanRepo planRepo;

    public List<Memory> findByLocation(String username, String location) {
        List<Plan> userPlans = planRepo.findByUsernamesContaining(username);
        List<Long> historyIds = userPlans.stream()
            .map(plan -> plan.getHistory().getId())
            .collect(Collectors.toList());

        return memoryRepo.findByPlaceAndHistoryIds(location, historyIds);
    }

    public List<Memory> findByDate(String username, LocalDate date) {
        List<Plan> userPlans = planRepo.findByUsernamesContaining(username);
        List<Long> historyIds = userPlans.stream()
            .map(plan -> plan.getHistory().getId())
            .collect(Collectors.toList());

        return memoryRepo.findByDateAndHistoryIds(date, historyIds);
    }

    public List<Memory> findByDateAndPlace(String username, LocalDate date, String place) {
        List<Plan> userPlans = planRepo.findByUsernamesContaining(username);
        List<Long> historyIds = userPlans.stream()
            .map(plan -> plan.getHistory().getId())
            .collect(Collectors.toList());

        return memoryRepo.findByDateAndPlaceAndHistoryIds(date, place, historyIds);
    }

    public List<Memory> findByPlaceIgnoreCase(String place) {
        return memoryRepo.findByPlaceIgnoreCase(place);
    }

    public Memory save(Memory memory) {
        return memoryRepo.save(memory);
    }
}
