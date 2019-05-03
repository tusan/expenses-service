package com.piggybank.context;

import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

public class JdbcConnectionProvider {
    public static Connection forCurrentConfigs(ExternalConfReader externalConfReader) {
        return wrapCheckedException(() -> {
            Class.forName(externalConfReader.get("database.driver.name")
                    .orElseThrow(IllegalArgumentException::new));
            return DriverManager.getConnection(
                    externalConfReader.get("database.url").orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get("database.user").orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get("database.password").orElseThrow(IllegalArgumentException::new)
            );
        });

    }
}
