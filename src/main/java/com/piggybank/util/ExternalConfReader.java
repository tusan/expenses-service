package com.piggybank.util;

import java.util.Optional;

public interface ExternalConfReader {
    Optional<String> get(String prop);
}
