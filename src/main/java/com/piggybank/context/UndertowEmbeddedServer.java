package com.piggybank.context;

import com.piggybank.util.ExternalConfReader;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class UndertowEmbeddedServer {

    private final Undertow server;

    private UndertowEmbeddedServer(Undertow server) {
        this.server = server;
    }

    public static UndertowEmbeddedServer createAndConfigure(RoutingHandler routingHandler, ExternalConfReader externalConfReader) {
        final int port = externalConfReader
                .get("server.port")
                .map(Integer::parseInt)
                .orElse(8080);

        final String host = externalConfReader
                .get("server.host")
                .orElse("localhost");

        return new UndertowEmbeddedServer(Undertow.builder()
                .setHandler(routingHandler)
                .addHttpListener(port, host)
                .build());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
