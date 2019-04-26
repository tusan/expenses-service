package com.piggybank.context;

import java.util.Optional;

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

    public interface AppContext {
        UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader);
    }

    public interface ExternalConfReader {
        Optional<String> get(String prop);
    }

   public static class EnvExternalConfReader implements ExternalConfReader {
        @Override
        public Optional<String> get(String prop) {
            return Optional.ofNullable(System.getenv(prop));
        }
    }
}
