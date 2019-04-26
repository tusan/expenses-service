package com.piggybank;

import com.piggybank.context.EmbeddedServiceApp;
import com.piggybank.context.EmbeddedServiceApp.AppContext;
import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import com.piggybank.context.UndertowEmbeddedServer;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.model.JdbcExpenseRepository;
import com.piggybank.context.JdbcConnectionProvider;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

import java.sql.Connection;

import static com.piggybank.util.IOUtils.deserialize;
import static com.piggybank.util.IOUtils.serialize;

public class ExpensesApp {
    public static void main(String[] args) {
        new EmbeddedServiceApp(new ExpensesAppContext()).run();
    }

    static class ExpensesAppContext implements AppContext {
        @Override
        public UndertowEmbeddedServer createContext(final ExternalConfReader externalConfReader) {
            final Connection databaseConnection = JdbcConnectionProvider.forCurrentConfigs(externalConfReader);
            final ExpenseRepository expenseRepository = new JdbcExpenseRepository(databaseConnection);

            final RoutingHandler routingHandlers =
                    new RoutingHandler()
                            .get("/expenses", exchange -> {
                                exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/json");
                                exchange.getResponseSender()
                                        .send(serialize(expenseRepository.getAllExpenses()));
                            })
                            .put("/expense", exchange -> exchange.getRequestReceiver()
                                    .receiveFullBytes((ex, message) -> {
                                        expenseRepository.save(deserialize(message, Expense.class));
                                        ex.setStatusCode(201);
                                    }));

            return UndertowEmbeddedServer.createAndConfigure(
                    routingHandlers,
                    externalConfReader
            );
        }
    }
}
