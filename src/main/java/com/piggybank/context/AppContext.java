package com.piggybank.context;

import com.piggybank.util.ExternalConfReader;

public interface AppContext {
    UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader);
}