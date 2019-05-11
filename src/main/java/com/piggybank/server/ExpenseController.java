package com.piggybank.server;

import com.piggybank.model.Expense;
import com.piggybank.service.ExpenseService;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.time.LocalDate;
import java.util.Deque;
import java.util.Optional;

import static com.piggybank.util.IOUtils.deserialize;
import static com.piggybank.util.IOUtils.serialize;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

class ExpenseController {
    private ExpenseService expenseService;

    ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private static Optional<String> parseQueryParam(HttpServerExchange exchange, String param) {
        return Optional.ofNullable(exchange.getQueryParameters()
                .get(param))
                .map(Deque::getFirst);
    }

    void loadAll(HttpServerExchange exchange) {
        final LocalDate dateStart = parseQueryParam(exchange, "date-start")
                .map(date -> LocalDate.parse(date, BASIC_ISO_DATE))
                .orElse(LocalDate.EPOCH);

        final LocalDate dateEnd = parseQueryParam(exchange, "date-end")
                .map(date -> LocalDate.parse(date, BASIC_ISO_DATE))
                .orElse(LocalDate.now());

        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/json");
        exchange.getResponseSender()
                .send(serialize(expenseService.getAllExpenses(dateStart, dateEnd)));
    }

    void save(HttpServerExchange exchange) {
        exchange.getRequestReceiver()
                .receiveFullBytes((ex, message) -> {
                    expenseService.save(deserialize(message, Expense.class));
                    ex.setStatusCode(201);
                });
    }
}
