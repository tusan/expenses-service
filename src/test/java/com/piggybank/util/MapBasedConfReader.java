package com.piggybank.util;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class MapBasedConfReader implements ExternalConfReader {
    private final Map<String, String> properties;

    public MapBasedConfReader(final Map<String, String> properties) {
        this.properties = Collections.unmodifiableMap(properties);
    }

    @Override
    public Optional<String> get(final String prop) {
        return Optional.ofNullable(properties.get(prop));
    }
}
