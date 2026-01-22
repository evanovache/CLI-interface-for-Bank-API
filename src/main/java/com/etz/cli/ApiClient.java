package com.etz.cli;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String ping() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/apex-bank/api/ping"))
                            .GET()
                            .build();

            HttpResponse<String> response = 
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return "Status: " + response.statusCode() +
                   "\nBody: " + response.body();

        } catch (Exception e) {
            return "Error calling API: " + e.getMessage();
        }
    }
}
