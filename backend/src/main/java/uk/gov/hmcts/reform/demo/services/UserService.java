package uk.gov.hmcts.reform.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo testTableRepository;;;;

    public List<User> findAll() {
        return testTableRepository.findAll();
    }

    public User save(User testTable) {
        return testTableRepository.save(testTable);
    }
}
