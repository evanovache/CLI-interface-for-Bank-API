package com.etz.cli.http;

import java.net.http.HttpClient;

public class HttpClientProvider {

    private static final HttpClient CLIENT = HttpClient.newBuilder().build();

    private HttpClientProvider() {}

    public static HttpClient getClient() {
        return CLIENT;
    }
}
