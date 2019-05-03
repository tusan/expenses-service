package com.piggybank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.piggybank.ExpensesApp.ExpensesAppContext;
import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import com.piggybank.model.ResultSetConverter;
import com.piggybank.util.EmbeddedAppTestRule;
import com.piggybank.util.IOUtils;
import com.piggybank.util.InMemoryDatabaseRule;
import com.piggybank.util.MapBasedConfReader;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static com.piggybank.context.JdbcConnectionProvider.*;
import static com.piggybank.context.UndertowEmbeddedServer.SERVER_HOST;
import static com.piggybank.context.UndertowEmbeddedServer.SERVER_PORT;
import static java.util.Objects.requireNonNull;

public class ExpensesAppTest {

    private static final ExternalConfReader TEST_CONFIGURATIONS = new MapBasedConfReader(
            ImmutableMap.<String, String>builder()
                    .put(SERVER_PORT, "8081")
                    .put(SERVER_HOST, "localhost")
                    .put(DATABASE_DRIVER_NAME, "org.h2.Driver")
                    .put(DATABASE_URL, "jdbc:h2:~/test")
                    .put(DATABASE_USER, "sa")
                    .put(DATABASE_PASSWORD, "")
                    .build());

    @Rule
    public EmbeddedAppTestRule contextRule = new EmbeddedAppTestRule(TEST_CONFIGURATIONS, new ExpensesAppContext());

    @Rule
    public InMemoryDatabaseRule databaseRule = new InMemoryDatabaseRule(TEST_CONFIGURATIONS);

    @Test
    public void shouldSaveAnExpense() throws Exception {
        Expense expected = Expense.newBuilder()
                .owner("example@test.org")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .description("this is a test")
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();

        Assert.assertEquals(
                201,
                contextRule.restClient().put("http://localhost:8081/expense", expected)
                        .code()
        );

        ResultSet resultSet = databaseRule.executeQuery("select id, owner, type, description, date, amount from expenses where id = 1");
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(expected, ResultSetConverter.toExpense(resultSet));
    }

    @Test
    public void shouldRetrieveAnExpense() {
        databaseRule.executeUpdate("insert into expenses (owner, type, description, date, amount) " +
                "values('example@test.org', 'MOTORBIKE', 'highway milan', '2019-05-02', '5.4')");

        Response response = contextRule.restClient().get("http://localhost:8081/expenses");
        Assert.assertEquals(200, response.code());

        List<Expense> actual = IOUtils.deserialize(
                requireNonNull(response.body()).byteStream(), new TypeReference<List<Expense>>() {
                }
        );

        Assert.assertEquals(Collections.singletonList(Expense.newBuilder()
                .owner("example@test.org")
                .date(LocalDate.of(2019, Month.MAY, 2))
                .type(ExpenseType.MOTORBIKE)
                .description("highway milan")
                .amount(5.4)
                .build()), actual);
    }
}
