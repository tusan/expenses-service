package com.piggybank.util;

import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.ResultSet;

import static com.piggybank.context.JdbcConnectionProvider.forCurrentConfigs;
import static com.piggybank.util.ExceptionUtils.wrapCheckedException;
import static com.piggybank.util.IOUtils.readFileFromClassPath;

public class InMemoryDatabaseRule extends ExternalResource {

    private final Connection connection;

    public InMemoryDatabaseRule(final ExternalConfReader externalConfReader) {
        this.connection = forCurrentConfigs(externalConfReader);
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
