package com.etz.cli.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.etz.cli.config.ApiConfig;
import com.etz.cli.http.HttpClientProvider;

public class HealthTest {


    public String ping() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(ApiConfig.BASE_URL + "/ping"))
                                    .GET()
                                    .build();

            HttpResponse<String> response = HttpClientProvider.getClient()
                                                .send(request, HttpResponse.BodyHandlers.ofString());

            return "Status: " + response.statusCode() + 
                   "\nBody: " + response.body();

        } catch (Exception e) {
            return "Error calling API: " + e.getMessage();
        }
    }
}
