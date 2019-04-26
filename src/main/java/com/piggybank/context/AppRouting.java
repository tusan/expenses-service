package com.piggybank.context;

import io.undertow.server.RoutingHandler;

public interface AppRouting {
    RoutingHandler getHandlers();
}
