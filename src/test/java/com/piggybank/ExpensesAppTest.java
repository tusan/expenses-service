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
import com.piggybank.util.MockExternalConfReader;
import com.piggybank.util.InMemoryDatabaseRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class ExpensesAppTest {
    @Rule
    public ExpensesAppContextRule context = new ExpensesAppContextRule();

    @Test
    public void shouldSaveAndRetrieveAnExpense() throws Exception {
        Assert.assertEquals(
                201,
                doPut(Expense.newBuilder()
                        .owner("example@example.it")
                        .id(12345L)
                        .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                        .type(ExpenseType.MOTORBIKE)
                        .amount(24.5)
                        .build()).getResponseCode()
        );

        HttpURLConnection connection = doGet();
        Assert.assertEquals(200, connection.getResponseCode());

        List<Expense> actual = IOUtils.deserialize(
                connection.getInputStream(),
                new TypeReference<List<Expense>>() {
                }
        );

        Assert.assertEquals(Collections.singletonList(Expense.newBuilder()
                .id(1L)
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build()), actual);
    }

    private HttpURLConnection doPut(Object body) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/expense").openConnection();
        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        wr.writeBytes(IOUtils.serialize(body));
        wr.flush();
        wr.close();

        return connection;
    }

    private HttpURLConnection doGet() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/expenses").openConnection();
        connection.setRequestMethod("GET");

        return connection;
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
        protected void before() throws Throwable{
            super.before();
            new EmbeddedServiceApp(this, externalConfReader).run();
        }

        @Override
        public UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader) {
            embeddedServer = delegate.createContext(externalConfReader);
            return embeddedServer;
        }
    }
}
