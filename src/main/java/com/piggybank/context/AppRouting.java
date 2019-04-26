package com.piggybank.context;

import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.util.IOUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

public class AppRouting {
    private final ExpenseRepository expenseRepository;

    public AppRouting(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public RoutingHandler getHandlers() {
        return new RoutingHandler()
                .get("/expenses", this::getAllExpensesHandler)
                .put("/expense", this::saveExpensesHandler);
    }

    private void saveExpensesHandler(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes((exchange1, message) -> {
            expenseRepository.save(IOUtils.deserialize(message, Expense.class));
            exchange.setStatusCode(201);
        });
    }

    private void getAllExpensesHandler(HttpServerExchange exchange) {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/json");
        exchange.getResponseSender().send(IOUtils.serialize(expenseRepository.getAllExpenses()));
    }
}