package uk.gov.hmcts.reform.demo.services;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCache implements Cache {

    private final ConcurrentMap<String, List<String>> cache = new ConcurrentHashMap<>();

    @Override
    public List<String> get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, List<String> value) {
        cache.put(key, value);
    }
}

