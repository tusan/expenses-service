package com.piggybank;

import org.junit.*;

import java.sql.*;

public class DatabaseTest {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASS = "";

    private static Connection conn;

    @BeforeClass
    public static void setUpDb() throws Exception{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        Statement stmt = conn.createStatement();
        String sql =  "CREATE TABLE IF NOT EXISTS expenses(\n" +
                "    id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "    owner VARCHAR(255) NOT NULL,\n" +
                "    type VARCHAR(255) NOT NULL,\n" +
                "    description VARCHAR(255),\n" +
                "    date TIMESTAMP NOT NULL,\n" +
                "    amount DOUBLE NOT NULL,\n" +
                "    PRIMARY KEY (id)\n" +
                ")";
        stmt.executeUpdate(sql);
        System.out.println("Created table in given database...");
    }

    @AfterClass
    public static void tearDownDb() throws SQLException {
        conn.createStatement().executeUpdate("DROP TABLE expenses");
        System.out.println("Dropped table in given database...");
    }

    @Test
    public void shouldStartDatabase() throws ClassNotFoundException, SQLException {
        Statement stmt = conn.createStatement();

        Assert.assertFalse(stmt.executeQuery("select * from expenses").next());
        ResultSet resultSet = stmt.executeQuery("select count(*) from expenses");

        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(0, resultSet.getInt(1));
    }
}

