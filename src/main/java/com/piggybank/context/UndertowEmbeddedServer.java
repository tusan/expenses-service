package com.piggybank.context;

import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class UndertowEmbeddedServer {

    private final Undertow server;

    private UndertowEmbeddedServer(Undertow server) {
        this.server = server;
    }

    public static UndertowEmbeddedServer createAndConfigure(final RoutingHandler routingHandlers,
                                                            final ExternalConfReader externalConfReader) {
        final int port = externalConfReader
                .get("server.port")
                .map(Integer::parseInt)
                .orElse(8080);

        final String host = externalConfReader
                .get("server.host")
                .orElse("localhost");

        return new UndertowEmbeddedServer(Undertow.builder()
                .setHandler(routingHandlers)
                .addHttpListener(port, host)
                .build());
    }

    void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
