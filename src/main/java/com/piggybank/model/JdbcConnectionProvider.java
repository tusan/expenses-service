package com.piggybank.model;

import com.piggybank.util.ExternalConfReader;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

public class JdbcConnectionProvider {
    public final static String DATABASE_URL = "DATABASE_URL";
    public final static String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME";
    public final static String DATABASE_USER = "DATABASE_USER";
    public final static String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    private final ExternalConfReader externalConfReader;

    public JdbcConnectionProvider(ExternalConfReader externalConfReader) {
        this.externalConfReader = externalConfReader;
    }

    public Connection provide() {
        return wrapCheckedException(() -> {
            Class.forName(externalConfReader.get(DATABASE_DRIVER_NAME)
                    .orElseThrow(IllegalArgumentException::new));
            return DriverManager.getConnection(
                    externalConfReader.get(DATABASE_URL).orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get(DATABASE_USER).orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get(DATABASE_PASSWORD).orElseThrow(IllegalArgumentException::new)
            );
        });

    }
}
