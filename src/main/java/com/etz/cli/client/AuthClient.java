package com.etz.cli.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.etz.cli.config.ApiConfig;
import com.etz.cli.http.ApiResponse;
import com.etz.cli.http.ErrorResponse;
import com.etz.cli.http.HttpClientProvider;
import com.etz.cli.model.User;
import com.etz.dto.LoginRequest;
import com.etz.dto.Pin;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthClient {
    
    private final ObjectMapper mapper = new ObjectMapper();

    public ApiResponse login(String email, String password) throws Exception {
        try {
            LoginRequest loginRequest = new LoginRequest(email, password);

            String json = mapper.writeValueAsString(loginRequest);

            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(ApiConfig.BASE_URL + "/users/login"))
                                    .header("Content-Type", "application/json")
                                    .POST(HttpRequest.BodyPublishers.ofString(json))
                                    .build();

            HttpResponse<String> response = HttpClientProvider.getClient()
                                                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                User user = mapper.readValue(response.body(), User.class);
                return ApiResponse.user(response.statusCode(), user);
            }

            ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);

            return ApiResponse.error(response.statusCode(), error);

        } catch (Exception e) {
        ErrorResponse error = new ErrorResponse("Client error: " + e.getMessage());
        return ApiResponse.error(500, error);
        }
    }

    public void validatePin(long accountNumber, String pin) {
        try {
            Pin p = new Pin(pin);
            String json = mapper.writeValueAsString(p); 
            
            HttpRequest request = HttpRequest.newBuilder() 
                            .uri(URI.create(ApiConfig.BASE_URL + "/accounts/" + accountNumber + "/validate"))
                            .header("Content-Type", "application/json") 
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();

            HttpResponse<String> response = HttpClientProvider.getClient() 
                        .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 401) 
                throw new IllegalArgumentException("Invalid Pin");
        } catch (Exception e) {
            System.out.println("Validation Failed");
        }
    }
}
