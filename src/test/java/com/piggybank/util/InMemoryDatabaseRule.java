package com.piggybank.util;

import org.junit.rules.ExternalResource;

import javax.sql.DataSource;
import java.sql.ResultSet;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;
import static com.piggybank.util.IOUtils.readFileFromClassPath;

public class InMemoryDatabaseRule extends ExternalResource {

    private final DataSource dataSource;

    public InMemoryDatabaseRule(final DataSource dataSource) {
        this.dataSource = dataSource;
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
        wrapCheckedException(() -> dataSource.getConnection()
                .createStatement()
                .executeUpdate(query));
    }

    public ResultSet executeQuery(String query) {
        return wrapCheckedException(() -> dataSource.getConnection()
                .createStatement()
                .executeQuery(query));
    }
}
