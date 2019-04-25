package com.piggybank;

import com.piggybank.util.TestDatabase;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {
    @Rule
    public TestDatabase db = new TestDatabase();

    @Test
    public void shouldStartDatabase() throws SQLException {
        Statement stmt = db.getConnection().createStatement();

        Assert.assertFalse(stmt.executeQuery("select * from expenses").next());
        ResultSet resultSet = stmt.executeQuery("select count(*) from expenses");

        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(0, resultSet.getInt(1));
    }

    @Test
    public void shouldStartDatabase2() throws SQLException {
        Statement stmt = db.getConnection().createStatement();

        Assert.assertFalse(stmt.executeQuery("select * from expenses").next());
        ResultSet resultSet = stmt.executeQuery("select count(*) from expenses");

        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(0, resultSet.getInt(1));
    }
}

