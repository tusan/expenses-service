package com.piggybank.util;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.piggybank.util.IOUtils.readFileFromClassPath;

public class InMemoryDatabaseRule extends ExternalResource {

    private Connection dbConnection;

    protected InMemoryDatabaseRule() {
        try {
            Class.forName("org.h2.Driver");
            dbConnection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void before() throws Throwable {
        executeUpdate(readFileFromClassPath("schema.sql"));
    }

    @Override
    protected void after() {
        try {
            executeUpdate("DROP TABLE IF EXISTS expenses");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void executeUpdate(String query) throws SQLException {
        dbConnection.createStatement().executeUpdate(query);
    }
}
