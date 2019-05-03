package com.piggybank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.piggybank.ExpensesApp.ExpensesAppContext;
import com.piggybank.context.EmbeddedServiceApp;
import com.piggybank.context.EmbeddedServiceApp.AppContext;
import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import com.piggybank.context.UndertowEmbeddedServer;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import com.piggybank.util.IOUtils;
import com.piggybank.util.InMemoryDatabaseRule;
import com.piggybank.util.MockExternalConfReader;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.piggybank.util.IOUtils.readFileFromClassPath;
import static com.piggybank.util.IOUtils.serialize;
import static java.util.Objects.requireNonNull;

public class ExpensesAppTest {
    private static final OkHttpClient client = new OkHttpClient();
    private static final TypeReference<List<Expense>> EXPENSES_LIST_TYPE = new TypeReference<List<Expense>>() {
    };

    @Rule
    public ExpensesAppContextRule context = new ExpensesAppContextRule();

    @Test
    public void shouldSaveAndRetrieveAnExpense() throws Exception {
        Assert.assertEquals(
                201,
                createExpense(Expense.newBuilder()
                        .owner("example@example.it")
                        .id(12345L)
                        .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                        .type(ExpenseType.MOTORBIKE)
                        .amount(24.5)
                        .build())
                        .code()
        );

        Response response = fetchAll();
        Assert.assertEquals(200, response.code());

        List<Expense> actual = IOUtils.deserialize(
                requireNonNull(response.body()).byteStream(), EXPENSES_LIST_TYPE
        );

        Assert.assertEquals(Collections.singletonList(Expense.newBuilder()
                .id(5L)
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build()), actual.stream().filter(exp -> exp.id() == 5).collect(Collectors.toList()));
    }

    private Response createExpense(Object body) throws IOException {
        return client.newCall(new Request.Builder()
                .url("http://localhost:8081/expense")
                .put(RequestBody.create(MediaType.get("application/json"), serialize(body)))
                .build())
                .execute();
    }

    private Response fetchAll() throws IOException {
        return client.newCall(new Request.Builder()
                .url("http://localhost:8081/expenses")
                .build())
                .execute();
    }


    private class ExpensesAppContextRule extends InMemoryDatabaseRule implements AppContext {
        private AppContext delegate;
        private UndertowEmbeddedServer embeddedServer;
        private ExternalConfReader externalConfReader;

        ExpensesAppContextRule() {
            MockExternalConfReader externalConfReader = new MockExternalConfReader();

            externalConfReader.set("server.port", "8081");
            externalConfReader.set("server.host", "localhost");
            externalConfReader.set("database.driver.name", "org.h2.Driver");
            externalConfReader.set("database.url", "jdbc:h2:~/test");
            externalConfReader.set("database.user", "sa");
            externalConfReader.set("database.password", "");

            this.externalConfReader = externalConfReader;
            this.delegate = new ExpensesAppContext();
        }

        @Override
        protected void after() {
            super.after();
            embeddedServer.stop();
        }

        @Override
        protected void before() throws Throwable {
            super.before();
            super.executeUpdate(readFileFromClassPath("data-test.sql"));
            new EmbeddedServiceApp(this, externalConfReader).run();
        }

        @Override
        public UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader) {
            embeddedServer = delegate.createContext(externalConfReader);
            return embeddedServer;
        }
    }
}
