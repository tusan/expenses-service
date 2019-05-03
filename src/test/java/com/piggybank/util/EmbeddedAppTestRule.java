package com.piggybank.util;

import com.piggybank.context.EmbeddedServiceApp;
import com.piggybank.context.EmbeddedServiceApp.AppContext;
import com.piggybank.context.EmbeddedServiceApp.ExternalConfReader;
import com.piggybank.context.UndertowEmbeddedServer;
import okhttp3.*;
import org.junit.rules.ExternalResource;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;
import static com.piggybank.util.IOUtils.serialize;

public class EmbeddedAppTestRule extends ExternalResource implements AppContext {
    private final AppContext delegate;
    private final ExternalConfReader externalConfReader;
    private final RestClient restClient;

    private UndertowEmbeddedServer server;

    public EmbeddedAppTestRule(final ExternalConfReader externalConfReader, final AppContext delegate) {
        this.externalConfReader = externalConfReader;
        this.delegate = delegate;
        this.restClient = new RestClient();
    }

    @Override
    public UndertowEmbeddedServer createContext(ExternalConfReader externalConfReader) {
        server = delegate.createContext(externalConfReader);
        return server;
    }

    @Override
    protected void before() {
        new EmbeddedServiceApp(this, externalConfReader).run();
    }

    @Override
    protected void after() {
        server.stop();
    }

    public RestClient restClient() {
        return restClient;
    }

    public static class RestClient {
        private static final OkHttpClient client = new OkHttpClient();

        private RestClient() {
        }

        public Response put(String url, Object body) {
            return wrapCheckedException(() -> client.newCall(new Request.Builder()
                    .url(url)
                    .put(RequestBody.create(MediaType.get("application/json"), serialize(body)))
                    .build())
                    .execute());

        }

        public Response get(String url) {
            return wrapCheckedException(() -> client.newCall(new Request.Builder()
                    .url(url)
                    .build())
                    .execute());
        }
    }
}
