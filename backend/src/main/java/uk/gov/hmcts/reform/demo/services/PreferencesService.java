package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;

import java.util.List;

@Service
public class PreferencesService {

    @Autowired
    private PreferencesRepo preferencesRepo;

    public List<Preferences> findAllPreferences() {
        return preferencesRepo.findAll();
    }

    public Preferences save(Preferences preferences) {
        return preferencesRepo.save(preferences);
    }
}
