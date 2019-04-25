package com.piggybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.piggybank.server.EmbeddedServer;
import com.piggybank.server.UndertowEmbeddedServer;
import com.piggybank.util.EnvExternalConfReader;
import com.piggybank.util.ExternalConfReader;

public class ExpensesApp {
    private final ExpensesAppContext configurations;

    ExpensesApp(ExpensesAppContext configurations) {
        this.configurations = configurations;
    }

    public static void main(String[] args) {
        ExpensesApp app = new ExpensesApp(ExpensesAppContext.createDefault());
        app.run();
    }

    void run() {
        configurations.getEmbeddedServer().start();
    }

    static class ExpensesAppContext {
        private final ObjectMapper mapper;
        private final EmbeddedServer embeddedServer;

        private ExpensesAppContext(ObjectMapper mapper, EmbeddedServer embeddedServer) {
            this.mapper = mapper;
            this.embeddedServer = embeddedServer;
        }

        static ExpensesAppContext createWithExternalConfReader(ExternalConfReader externalConfReader) {
            Preconditions.checkNotNull(externalConfReader);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return new ExpensesAppContext(
                    mapper,
                    UndertowEmbeddedServer.createAndConfigure(mapper,
                            externalConfReader.get("server.port").map(Integer::parseInt).orElse(8080),
                            externalConfReader.get("").orElse("localhost"))
            );
        }

        static ExpensesAppContext createDefault() {
            return createWithExternalConfReader(new EnvExternalConfReader());
        }

        ObjectMapper getMapper() {
            return mapper;
        }

        EmbeddedServer getEmbeddedServer() {
            return embeddedServer;
        }
    }
}
