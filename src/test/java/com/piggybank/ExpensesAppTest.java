package com.piggybank;

import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import com.piggybank.util.IOUtils;
import com.piggybank.util.MockExternalConfReader;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;

import static com.piggybank.ExpensesApp.ExpensesAppContext.createWithExternalConfReader;

public class ExpensesAppTest {
    @Rule
    public ExpensesAppTestContext context = new ExpensesAppTestContext();

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
        return context.getContext()
                .getMapper()
                .readValue(IOUtils.inputStreamToString(con.getInputStream()),
                        Expense.class);
    }

    static class ExpensesAppTestContext extends ExternalResource {
        private static final ExpensesApp.ExpensesAppContext context = createWithExternalConfReader(new MockExternalConfReader());

        @Override
        protected void after() {
            context.getEmbeddedServer().stop();
        }

        @Override
        protected void before() {
            new ExpensesApp(context).run();
        }

        ExpensesApp.ExpensesAppContext getContext() {
            return context;
        }
    }
}
