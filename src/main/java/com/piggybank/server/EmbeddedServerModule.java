package com.piggybank.server;

import com.piggybank.service.ExpenseService;
import com.piggybank.util.ExternalConfReader;
import dagger.Module;
import dagger.Provides;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

import javax.inject.Singleton;

@Module
public class EmbeddedServerModule {
    public final static String SERVER_PORT = "SERVER_PORT";
    public final static String SERVER_HOST = "SERVER_HOST";

    @Provides
    @Singleton
    static Undertow provideServer(ExpenseController expenseController,
                                  ExternalConfReader externalConfReader) {
        final int port = externalConfReader
                .get(SERVER_PORT)
                .map(Integer::parseInt)
                .orElse(8080);

        final String host = externalConfReader
                .get(SERVER_HOST)
                .orElse("localhost");

        return Undertow.builder()
                .setHandler(new RoutingHandler()
                        .get("/expenses", expenseController::loadAll)
                        .put("/expense", expenseController::save))
                .addHttpListener(port, host)
                .build();
    }

    @Provides
    @Singleton
    static ExpenseController provideController(ExpenseService expenseService) {
        return new ExpenseController(expenseService);
    }
}