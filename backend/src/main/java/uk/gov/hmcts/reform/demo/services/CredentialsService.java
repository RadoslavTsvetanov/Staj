package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;

@Service
public class CredentialsService {

    @Autowired
    private CredentialsRepo credentialsRepo;

    public Credentials findByEmail(String email) {
        return credentialsRepo.findByEmail(email);
    }
}
