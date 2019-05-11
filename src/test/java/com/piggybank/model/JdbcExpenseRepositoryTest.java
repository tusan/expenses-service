package com.piggybank.model;

import com.google.common.collect.ImmutableMap;
import com.piggybank.util.ExternalConfReader;
import com.piggybank.util.InMemoryDatabaseRule;
import com.piggybank.util.MapBasedConfReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static com.piggybank.model.JdbcConnectionProvider.*;
import static com.piggybank.server.EmbeddedServerModule.SERVER_HOST;
import static com.piggybank.server.EmbeddedServerModule.SERVER_PORT;

public class JdbcExpenseRepositoryTest {
    private static final ExternalConfReader TEST_CONFIGURATIONS = new MapBasedConfReader(
            ImmutableMap.<String, String>builder()
                    .put(SERVER_PORT, "8081")
                    .put(SERVER_HOST, "localhost")
                    .put(DATABASE_DRIVER_NAME, "org.h2.Driver")
                    .put(DATABASE_URL, "jdbc:h2:~/test")
                    .put(DATABASE_USER, "sa")
                    .put(DATABASE_PASSWORD, "")
                    .build());

    private final JdbcConnectionProvider jdbcConnectionProvider = new JdbcConnectionProvider(TEST_CONFIGURATIONS);

    @Rule
    public InMemoryDatabaseRule databaseRule = new InMemoryDatabaseRule(jdbcConnectionProvider);

    private JdbcExpenseRepository sut;

    @Before
    public void setUp() {
        sut = new JdbcExpenseRepository(jdbcConnectionProvider);
    }

    @Test
    public void shouldGetAllExpensesFromDbOrderedByDateDesc() {
        databaseRule.executeUpdate("insert into expenses (owner, type, description, date, amount) values('example@test.org', 'MOTORBIKE', 'test 3', '2019-05-03', '5.4')");
        databaseRule.executeUpdate("insert into expenses (owner, type, description, date, amount) values('example@test.org', 'MOTORBIKE', 'test 4', '2019-05-04', '5.4')");

        final List<Expense> actual = sut.getAllExpenses();

        Assert.assertEquals(
                Arrays.asList(
                        Expense.newBuilder()
                                .owner("example@test.org")
                                .date(LocalDate.of(2019, Month.MAY, 4))
                                .type(ExpenseType.MOTORBIKE)
                                .description("test 4")
                                .amount(5.4)
                                .build(),
                        Expense.newBuilder()
                                .owner("example@test.org")
                                .date(LocalDate.of(2019, Month.MAY, 3))
                                .type(ExpenseType.MOTORBIKE)
                                .description("test 3")
                                .amount(5.4)
                                .build()
                ),
                actual
        );
    }

    @Test
    public void shouldSaveAnExpense() throws SQLException {
        Expense expected = Expense.newBuilder()
                .owner("example@test.org")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .description("this is a test")
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();

        sut.save(expected);

        ResultSet resultSet = databaseRule.executeQuery("select id, owner, type, description, date, amount from expenses where id = 1");
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(expected, ResultSetConverter.toExpense(resultSet));
    }
}