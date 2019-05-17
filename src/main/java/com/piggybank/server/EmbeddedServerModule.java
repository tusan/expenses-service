package com.piggybank.server;

import com.piggybank.service.ExpenseService;
import com.piggybank.util.ExternalConfReader;
import dagger.Module;
import dagger.Provides;
import io.undertow.Undertow;

import javax.inject.Singleton;

@Module
public class EmbeddedServerModule {
    public final static String SERVER_PORT = "SERVER_PORT";
    public final static String SERVER_HOST = "SERVER_HOST";

    @Provides
    @Singleton
    static Undertow provideServer(final ExpenseService expenseService,
                                  final ExternalConfReader externalConfReader) {
        final int port = externalConfReader
                .get(SERVER_PORT)
                .map(Integer::parseInt)
                .orElse(8080);

        final String host = externalConfReader
                .get(SERVER_HOST)
                .orElse("localhost");

        return Undertow.builder()
                .setHandler(new ExpenseController(expenseService)
                        .getRoutingHandlers())
                .addHttpListener(port, host)
                .build();
    }
}