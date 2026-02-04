package com.etz.cli.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.etz.cli.config.ApiConfig;
import com.etz.cli.http.ApiResponse;
import com.etz.cli.http.HttpClientProvider;
import com.etz.cli.model.User;
import com.etz.dto.ErrorResponse;
import com.etz.dto.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserClient {
    
    private ObjectMapper mapper = new ObjectMapper();

    public ApiResponse signUp(SignUpRequest request) throws Exception {
        try {
            String json = mapper.writeValueAsString(request);

            HttpRequest req = HttpRequest.newBuilder() 
                                        .uri(URI.create(ApiConfig.BASE_URL + "/users"))
                                        .header("Content-Type", "application/json")
                                        .POST(HttpRequest.BodyPublishers.ofString(json))
                                        .build();

            HttpResponse<String> response = HttpClientProvider.getClient()
                                            .send(req, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                User user = mapper.readValue(response.body(), User.class);
                return ApiResponse.user(response.statusCode(), user);
            }

            ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
            return ApiResponse.error(400, error);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client error: " + e.getMessage());
            return ApiResponse.error(500, error);
        }        
    }
}
