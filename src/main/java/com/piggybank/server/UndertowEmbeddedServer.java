package com.piggybank.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseType;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;

import java.time.LocalDate;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UndertowEmbeddedServer implements EmbeddedServer {
    private static final Logger logger = Logger.getLogger(UndertowEmbeddedServer.class.getName());

    private final Undertow server;
    private final int port;
    private final String host;

    private UndertowEmbeddedServer(Undertow server, int port, String host) {
        this.server = server;
        this.port = port;
        this.host = host;
    }

    public static EmbeddedServer createAndConfigure(ObjectMapper mapper, int port, String host) {
        return new UndertowEmbeddedServer(Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(Handlers.path().addPrefixPath("/hello", helloHandler(mapper)))
                .build(), port, host);
    }

    private static HttpHandler helloHandler(ObjectMapper mapper) {
        return exchange -> {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
            try {
                exchange.getResponseSender().send(mapper.writeValueAsString(Expense.newBuilder()
                        .owner("example@example.it")
                        .id(12345L)
                        .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                        .type(ExpenseType.MOTORBIKE)
                        .amount(24.5)
                        .build()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void start() {
        server.start();
        logger.log(Level.INFO, String.format("started on http://%s:%d", host, port));
    }

    @Override
    public void stop() {
        server.stop();
    }
}
