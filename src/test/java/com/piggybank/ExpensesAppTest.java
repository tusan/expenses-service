package com.piggybank;

import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Collectors;

public class ExpensesAppTest {
    private ExpensesApp.ExpensesAppConfigurations configurations = ExpensesApp.withDefaultConfiguration();

    @After
    public void tearDown() {
        configurations.getEmbeddedServer().stop();
    }

    @Test
    public void shouldRunTheEmbeddedServer() throws Exception {
        ExpensesApp.main(null);

        URL url = new URL("http://localhost:8080/hello");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Assert.assertEquals(200, con.getResponseCode());
    }

    @Test
    public void shouldSerializeTheExpenseObject() throws Exception {
        ExpensesApp.main(null);

        URL url = new URL("http://localhost:8080/hello");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Expense expected = Expense.newBuilder()
                .owner("example@example.it")
                .id(12345L)
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();

        Assert.assertEquals(expected, parseResponse(con));
    }

    private Expense parseResponse(HttpURLConnection con) throws IOException {
        return configurations.getMapper().readValue(new BufferedReader(
                new InputStreamReader(con.getInputStream()))
                .lines()
                .collect(Collectors.joining()), Expense.class);
    }
}
