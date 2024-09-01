package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.DateWindow;
import uk.gov.hmcts.reform.demo.repositories.DateWindowRepo;

import java.util.List;
import java.util.Optional;

@Service
public class DateWindowService {

    @Autowired
    private DateWindowRepo dateWindowRepository;

    public List<DateWindow> findAll() {
        return dateWindowRepository.findAll();
    }

    public DateWindow findById(Long id) {
        Optional<DateWindow> dateWindow = dateWindowRepository.findById(id);
        if (dateWindow.isPresent()) {
            return dateWindow.get();
        } else {
            throw new RuntimeException("DateWindow not found with id: " + id);
        }
    }

    public DateWindow save(DateWindow dateWindow) {
        return dateWindowRepository.save(dateWindow);
    }
}
