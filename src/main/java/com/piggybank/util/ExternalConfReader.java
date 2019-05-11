package com.piggybank.util;

import java.util.Optional;

public interface ExternalConfReader {
    Optional<String> get(String prop);

    class EnvExternalConfReader implements ExternalConfReader {
        @Override
        public Optional<String> get(String prop) {
            return Optional.ofNullable(System.getenv(prop));
        }
    }
}