package com.piggybank.util;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.ResultSet;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;
import static com.piggybank.util.IOUtils.readFileFromClassPath;

public class InMemoryDatabaseRule extends ExternalResource {

    private final Connection connection;

    public InMemoryDatabaseRule(final JdbcConnectionProvider jdbcConnectionProvider) {
        this.connection = jdbcConnectionProvider.getConnection();
    }

    @Override
    protected void before() {
        readFileFromClassPath("schema.sql").ifPresent(this::executeUpdate);
    }

    @Override
    protected void after() {
        executeUpdate("DROP TABLE IF EXISTS expenses");
    }

    public void executeUpdate(final String query) {
        wrapCheckedException(() -> connection.createStatement().executeUpdate(query));
    }

    public ResultSet executeQuery(String query) {
        return wrapCheckedException(() -> connection.createStatement().executeQuery(query));
    }
}
