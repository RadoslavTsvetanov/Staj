package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;

import java.util.List;

@Service
public class CredentialsService {

    @Autowired
    private CredentialsRepo credentialsRepo;

    public Credentials findByEmail(String email) {
        return credentialsRepo.findByEmail(email);
    }

    public Credentials save(Credentials credentials) {
        if (credentialsRepo.existsByEmail(credentials.getEmail())) {
            throw new DuplicateEmailException("Email " + credentials.getEmail() + " already exists");
        }

        return credentialsRepo.save(credentials);
    }

    public List<Credentials> findAll() {
        return credentialsRepo.findAll();
    }
}
