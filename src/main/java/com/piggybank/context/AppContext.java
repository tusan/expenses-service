package com.piggybank.context;

public interface AppContext {
    UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader);
}