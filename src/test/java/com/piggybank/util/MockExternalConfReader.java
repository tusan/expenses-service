package com.piggybank.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockExternalConfReader implements ExternalConfReader {
    private Map<String, String> properties = new HashMap<>();
    @Override
    public Optional<String> get(String prop) {
        return Optional.ofNullable(properties.get(prop));
    }

    public void set(String prop, String value) {
        properties.put(prop, value);
    }
}