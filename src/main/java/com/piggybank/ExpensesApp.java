package com.piggybank;

import com.piggybank.context.AppContext;
import com.piggybank.context.AppRouting;
import com.piggybank.context.DatabaseConnectionProvider;
import com.piggybank.context.UndertowEmbeddedServer;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.model.JdbcExpenseRepository;
import com.piggybank.util.EnvExternalConfReader;
import com.piggybank.util.ExternalConfReader;

import java.sql.Connection;

public class ExpensesApp {
    private final AppContext context;
    private final ExternalConfReader externalConfReader;

    ExpensesApp(AppContext context, ExternalConfReader externalConfReader) {
        this.context = context;
        this.externalConfReader = externalConfReader;
    }

    public static void main(String[] args) {
        new ExpensesApp(new ExpensesAppContext(), new EnvExternalConfReader()).run();
    }

    void run() {
        context.createContext(externalConfReader).start();
    }

    static class ExpensesAppContext implements AppContext {
        private static AppRouting withExpensesAppRouting(ExpenseRepository expenseRepository) {
            return new AppRouting(expenseRepository);
        }

        private static UndertowEmbeddedServer createEmbeddedServer(AppRouting appRouting,
                                                                   ExternalConfReader externalConfReader) {
            return UndertowEmbeddedServer.createAndConfigure(
                    appRouting.getHandlers(),
                    externalConfReader
            );
        }

        private static Connection withDbConnection(ExternalConfReader externalConfReader) {
            return DatabaseConnectionProvider.provide(externalConfReader);
        }

        private static ExpenseRepository withExpenseRepository(Connection connection) {
            return new JdbcExpenseRepository(connection);
        }

        @Override
        public UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader) {
            return createEmbeddedServer(
                    withExpensesAppRouting(
                            withExpenseRepository(withDbConnection(externalConfReader))
                    ),
                    externalConfReader
            );
        }
    }
}
