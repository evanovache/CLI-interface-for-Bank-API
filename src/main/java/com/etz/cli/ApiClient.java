package com.etz.cli;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:8080/apex-bank/api";
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

    
    public ApiResponse login(String email, String password) {
        try {
            String jsonBody = """
                    {
                        "email": "%s", 
                        "password": "%s"    
                    }
                    """.formatted(email, password);

            HttpRequest request = HttpRequest.newBuilder() 
                                  .uri(URI.create(BASE_URL + "/users/login"))
                                  .header("Content-Type", "application/json")
                                  .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                  .build();

            HttpResponse<String> response = 
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            return new ApiResponse(500, "Client error: " + e.getMessage());
        }
    }
}
