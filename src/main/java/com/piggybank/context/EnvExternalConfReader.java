package com.piggybank.context;

import java.util.Optional;

public class EnvExternalConfReader implements ExternalConfReader {
    @Override
    public Optional<String> get(String prop) {
        return Optional.ofNullable(System.getenv(prop));
    }
}
