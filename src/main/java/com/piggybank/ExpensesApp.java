package com.piggybank;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpensesApp {
    private static final Logger logger = Logger.getLogger(ExpensesApp.class.getName());
    public void start() {
        int port = 8080;
        /*
         *  "localhost" will ONLY listen on local host.
         *  If you want the server reachable from the outside you need to set "0.0.0.0"
         */
        String host = "localhost";

        /*
         * This web server has a single handler with no routing.
         * ALL urls are handled by the helloWorldHandler.
         */
        Undertow server = Undertow.builder()
                // Add the helloWorldHandler as a method reference.
                .addHttpListener(port, host, exchange -> {
                    exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello World!");
                })
                .build();
        server.start();
        logger.log(Level.INFO, String.format("started on http://%s:%d", host, port));
    }
}
