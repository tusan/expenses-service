package com.piggybank.context;

public class EmbeddedServiceApp {
    private final AppContext context;
    private final ExternalConfReader externalConfReader;

    protected EmbeddedServiceApp(AppContext context, ExternalConfReader externalConfReader) {
        this.context = context;
        this.externalConfReader = externalConfReader;
    }

    public void run() {
        context.createContext(externalConfReader).start();
    }
}
