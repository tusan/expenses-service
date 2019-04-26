package com.piggybank.context;

import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnectionProvider {
    public static Connection forCurrentConfigs(ExternalConfReader externalConfReader) {
        try {
            Class.forName(externalConfReader.get("database.driver.name")
                    .orElseThrow(IllegalArgumentException::new));
            return DriverManager.getConnection(
                    externalConfReader.get("database.url").orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get("database.user").orElseThrow(IllegalArgumentException::new),
                    externalConfReader.get("database.password").orElseThrow(IllegalArgumentException::new)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
