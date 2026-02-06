package com.etz.cli.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.etz.cli.config.ApiConfig;
import com.etz.cli.http.ApiResponse;
import com.etz.cli.http.ErrorResponse;
import com.etz.cli.http.HttpClientProvider;
import com.etz.cli.model.User;
import com.etz.dto.Account;
import com.etz.dto.CreateAccountRequest;
import com.etz.dto.SignUpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserClient {
    
    private ObjectMapper mapper = new ObjectMapper();

    public ApiResponse signUp(SignUpRequest request) {
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


    public List<ApiResponse> listAccounts(int id) {
        List<ApiResponse> res = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(ApiConfig.BASE_URL + "/users/" + id + "/accounts"))
                                             .GET()
                                             .build();

            HttpResponse<String> response = HttpClientProvider.getClient()
                                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Account> accounts = mapper.readValue(response.body(), 
                        new TypeReference<List<Account>>() {});
                
                for (Account account: accounts) {                    
                    res.add(ApiResponse.account(account));
                }
        
                return res;
            }

            ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
            res.add(ApiResponse.error(400, error));
            return res;

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client error: " + e.getMessage());
            res.add(ApiResponse.error(500, error));
            return res;
        }
    } 


     public ApiResponse createAccount(CreateAccountRequest req, int id) {
        try {
            String json = mapper.writeValueAsString(req);

            HttpRequest request = HttpRequest.newBuilder() 
                                    .uri(URI.create(ApiConfig.BASE_URL + "/users/" + id + "/accounts"))
                                    .header("Content-Type", "application/json")
                                    .POST(HttpRequest.BodyPublishers.ofString(json))
                                    .build();
                                
            HttpResponse<String> response = HttpClientProvider.getClient() 
                                            .send(request, HttpResponse.BodyHandlers.ofString()); 

            if (response.statusCode() == 201) {
                Account account = mapper.readValue(response.body(), Account.class);
                return ApiResponse.account(account);
            }

            ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
            return ApiResponse.error(400, error);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client Error: " + e.getMessage());
            return ApiResponse.error(500, error);
        }
     }
}
