package com.piggybank.context;

public class EmbeddedServiceApp {
    private final AppContext context;
    private final ExternalConfReader externalConfReader;

    public EmbeddedServiceApp(AppContext context, ExternalConfReader externalConfReader) {
        this.context = context;
        this.externalConfReader = externalConfReader;
    }

    public EmbeddedServiceApp(AppContext context) {
        this(context, new EnvExternalConfReader());
    }

    public void run() {
        context.createContext(externalConfReader).start();
    }
}
