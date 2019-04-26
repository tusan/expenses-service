package com.piggybank.util;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InMemoryDatabaseRule extends ExternalResource {

    private Connection conn;

    public InMemoryDatabaseRule() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void before() throws Throwable {
        conn.createStatement()
                .executeUpdate(IOUtils.inputStreamToString(getClass()
                        .getClassLoader()
                        .getResourceAsStream("schema.sql")));
    }

    @Override
    protected void after() {
        try {
            conn.createStatement().executeUpdate("DROP TABLE IF EXISTS expenses");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
