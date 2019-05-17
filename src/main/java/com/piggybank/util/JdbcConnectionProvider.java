package com.piggybank.util;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

public class JdbcConnectionProvider {
    public final static String DATABASE_URL = "DATABASE_URL";
    public final static String DATABASE_USER = "DATABASE_USER";
    public final static String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    private static final Object LOCK = new Object();
    private static DataSource dataSource;

    private final ExternalConfReader externalConfReader;

    public JdbcConnectionProvider(ExternalConfReader externalConfReader) {
        this.externalConfReader = externalConfReader;
    }

    public Connection getConnection() {
        synchronized (LOCK) {
            if (dataSource == null) {
                dataSource = createDatasource();
            }
        }

        return wrapCheckedException(() -> dataSource.getConnection());
    }

    private DataSource createDatasource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(externalConfReader.get(DATABASE_URL).orElseThrow(IllegalArgumentException::new));
        dataSource.setUsername(externalConfReader.get(DATABASE_USER).orElseThrow(IllegalArgumentException::new));
        dataSource.setPassword(externalConfReader.get(DATABASE_PASSWORD).orElseThrow(IllegalArgumentException::new));

        return dataSource;
    }
}
