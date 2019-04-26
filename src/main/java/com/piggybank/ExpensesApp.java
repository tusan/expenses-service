package com.piggybank;

import com.piggybank.context.*;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.model.JdbcExpenseRepository;
import com.piggybank.util.IOUtils;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

import java.sql.Connection;

public class ExpensesApp extends EmbeddedServiceApp {
    ExpensesApp(AppContext context, ExternalConfReader externalConfReader) {
        super(context, externalConfReader);
    }

    public static void main(String[] args) {
        new ExpensesApp(new ExpensesAppContext(), new EnvExternalConfReader()).run();
    }

    static class ExpensesAppContext implements AppContext {
        @Override
        public UndertowEmbeddedServer createContext(final ExternalConfReader externalConfReader) {
            final Connection databaseConnection = JdbcConnectionProvider.forCurrentConfigs(externalConfReader);
            final ExpenseRepository expenseRepository = new JdbcExpenseRepository(databaseConnection);
            final AppRouting expenseAppRouting = new ExpenseAppRouting(expenseRepository);

            return UndertowEmbeddedServer.createAndConfigure(
                    expenseAppRouting,
                    externalConfReader
            );
        }
    }

    static class ExpenseAppRouting implements AppRouting {
        private final ExpenseRepository expenseRepository;

        ExpenseAppRouting(ExpenseRepository expenseRepository) {
            this.expenseRepository = expenseRepository;
        }

        @Override
        public RoutingHandler getHandlers() {
            return new RoutingHandler()
                    .get("/expenses", exchange -> {
                        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/json");
                        exchange.getResponseSender().send(IOUtils.serialize(expenseRepository.getAllExpenses()));
                    })
                    .put("/expense", exchange -> exchange.getRequestReceiver()
                            .receiveFullBytes((ex, message) -> {
                                expenseRepository.save(IOUtils.deserialize(message, Expense.class));
                                ex.setStatusCode(201);
                            }));
        }

    }
}
