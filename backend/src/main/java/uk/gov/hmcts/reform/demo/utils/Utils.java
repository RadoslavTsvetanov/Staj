package uk.gov.hmcts.reform.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    public <T> String JsonStringify(T object) {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            return objMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle this better in a real-world scenario
        }
    }
}
