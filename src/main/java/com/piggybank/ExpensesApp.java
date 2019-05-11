package com.piggybank;

import com.piggybank.context.EmbeddedServiceApp;
import com.piggybank.context.EmbeddedServiceApp.AppContext;
import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import com.piggybank.context.JdbcConnectionProvider;
import com.piggybank.context.UndertowEmbeddedServer;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepositoryFactory;
import com.piggybank.model.JdbcExpenseRepositoryFactory;
import com.piggybank.service.ExpenseService;
import com.piggybank.service.ExpenseServiceImpl;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

import java.time.LocalDate;
import java.util.Deque;
import java.util.Optional;

import static com.piggybank.util.IOUtils.deserialize;
import static com.piggybank.util.IOUtils.serialize;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

public class ExpensesApp {
    public static void main(String[] args) {
        new EmbeddedServiceApp(new ExpensesAppContext()).run();
    }

    static class ExpensesAppContext implements AppContext {
        @Override
        public UndertowEmbeddedServer createContext(final ExternalConfReader externalConfReader) {
            final JdbcConnectionProvider jdbcConnectionProvider = new JdbcConnectionProvider(externalConfReader);
            final ExpenseRepositoryFactory expenseRepositoryFactory = new JdbcExpenseRepositoryFactory(jdbcConnectionProvider);
            final ExpenseService expenseService = new ExpenseServiceImpl(expenseRepositoryFactory);

            final RoutingHandler routingHandlers =
                    new RoutingHandler()
                            .get("/expenses", exchange -> {
                                final LocalDate dateStart = parseQueryParam(exchange, "date-start")
                                        .map(date -> LocalDate.parse(date, BASIC_ISO_DATE))
                                        .orElse(LocalDate.EPOCH);

                                final LocalDate dateEnd = parseQueryParam(exchange, "date-end")
                                        .map(date -> LocalDate.parse(date, BASIC_ISO_DATE))
                                        .orElse(LocalDate.now());

                                exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/json");
                                exchange.getResponseSender()
                                        .send(serialize(expenseService.getAllExpenses(dateStart, dateEnd)));
                            })
                            .put("/expense", exchange -> exchange.getRequestReceiver()
                                    .receiveFullBytes((ex, message) -> {
                                        expenseService.save(deserialize(message, Expense.class));
                                        ex.setStatusCode(201);
                                    }));

            return UndertowEmbeddedServer.createAndConfigure(
                    routingHandlers,
                    externalConfReader
            );
        }

        private Optional<String> parseQueryParam(HttpServerExchange exchange, String param) {
            return Optional.ofNullable(exchange.getQueryParameters()
                    .get(param))
                    .map(Deque::getFirst);
        }
    }
}
