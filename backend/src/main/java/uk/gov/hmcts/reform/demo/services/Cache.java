package uk.gov.hmcts.reform.demo.services;

import java.util.List;

public interface Cache {
    List<String> get(String key);
    void put(String key, List<String> value);
}
