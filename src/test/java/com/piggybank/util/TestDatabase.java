package com.piggybank.util;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabase extends ExternalResource {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection conn;

    public TestDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
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

    public Connection getConnection() {
        return conn;
    }
}
