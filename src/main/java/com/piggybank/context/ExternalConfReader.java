package com.piggybank.context;

import java.util.Optional;

public interface ExternalConfReader {
    Optional<String> get(String prop);
}
