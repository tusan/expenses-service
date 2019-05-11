package com.piggybank.util;

import io.undertow.Undertow;
import okhttp3.*;
import org.junit.rules.ExternalResource;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;
import static com.piggybank.util.IOUtils.serialize;

public class EmbeddedAppTestRule extends ExternalResource {
    private final Undertow server;
    private final RestClient restClient;

    public EmbeddedAppTestRule(final Undertow server) {
        this.server = server;
        this.restClient = new RestClient();
    }

    @Override
    protected void before() {
        server.start();
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
