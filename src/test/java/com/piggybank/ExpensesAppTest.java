package com.piggybank;

import com.piggybank.ExpensesApp.ExpensesAppContext;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import com.piggybank.util.MockExternalConfReader;
import org.junit.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Collectors;

import static com.piggybank.ExpensesApp.ExpensesAppContext.createWithExternalConfReader;

public class ExpensesAppTest {
    private static final ExpensesAppContext configurations = createWithExternalConfReader(new MockExternalConfReader());

    @BeforeClass
    public static void setUpBeforeClass() {
        new ExpensesApp(configurations).run();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        configurations.getEmbeddedServer().stop();
    }

    @Test
    public void shouldRunTheEmbeddedServer() throws Exception {
        HttpURLConnection con = makeGetCall();

        Assert.assertEquals(200, con.getResponseCode());
    }

    @Test
    public void shouldSerializeTheExpenseObject() throws Exception {
        HttpURLConnection con = makeGetCall();

        Expense expected = Expense.newBuilder()
                .owner("example@example.it")
                .id(12345L)
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();

        Assert.assertEquals(expected, parseResponse(con));
    }

    private HttpURLConnection makeGetCall() throws IOException {
        URL url = new URL("http://localhost:8080/hello");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        return con;
    }

    private Expense parseResponse(HttpURLConnection con) throws IOException {
        return configurations.getMapper().readValue(new BufferedReader(
                new InputStreamReader(con.getInputStream()))
                .lines()
                .collect(Collectors.joining()), Expense.class);
    }
}
