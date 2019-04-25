package com.piggybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.piggybank.server.EmbeddedServer;
import com.piggybank.server.UndertowEmbeddedServer;

public class ExpensesApp {
    private static final Object MUTEX = new Object();
    private static ExpensesAppConfigurations configurations = null;

    public static void main(String[] args) {
        ExpensesApp
                .withDefaultConfiguration()
                .getEmbeddedServer()
                .start();
    }

    static ExpensesAppConfigurations withDefaultConfiguration() {
        synchronized (MUTEX) {
            if (configurations == null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                configurations = new ExpensesAppConfigurations(mapper, UndertowEmbeddedServer.createAndConfigure(mapper, 8080, "localhost"));
            }
        }

        return configurations;
    }

    static class ExpensesAppConfigurations {
        private final ObjectMapper mapper;
        private final EmbeddedServer embeddedServer;

        private ExpensesAppConfigurations(ObjectMapper mapper, EmbeddedServer embeddedServer) {
            this.mapper = mapper;
            this.embeddedServer = embeddedServer;
        }

        ObjectMapper getMapper() {
            return mapper;
        }

        EmbeddedServer getEmbeddedServer() {
            return embeddedServer;
        }
    }
}
