package com.etz.cli.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.etz.cli.config.ApiConfig;
import com.etz.cli.http.AccountResponse;
import com.etz.cli.http.ErrorResponse;
import com.etz.cli.http.HttpClientProvider;
import com.etz.cli.model.MiniStatement;
import com.etz.dto.Balance;
import com.etz.dto.Pin;
import com.etz.dto.StatementRequest;
import com.etz.dto.TransactionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AccountClient {
    
    ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    } 
    
    public AccountResponse balance(long accountNumber, String pin) {
        try {

            Pin p = new Pin(pin);
            String json = mapper.writeValueAsString(p);
            
            HttpRequest request = HttpRequest.newBuilder() 
                        .uri(URI.create(ApiConfig.BASE_URL + "/accounts/" + accountNumber + "/balance"))
                        .header("Content-Type", "application/json") 
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

            HttpResponse<String> response = HttpClientProvider.getClient() 
                                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Balance balance = mapper.readValue(response.body(), Balance.class);
                return AccountResponse.balance(balance);
            }

            if (response.statusCode() == 401) {
                ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
                return AccountResponse.error(401, error);
            }

            ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
            return AccountResponse.error(400, error);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client Error: " + e.getMessage());
            return AccountResponse.error(500, error);
        }
    }


    public AccountResponse deposit(long accountNumber, TransactionRequest req) {
        try {
            String json = mapper.writeValueAsString(req);

            HttpRequest request = HttpRequest.newBuilder() 
                    .uri(URI.create(ApiConfig.BASE_URL + "/accounts/" + accountNumber + "/deposit"))
                    .header("Content-Type", "application/json") 
                    .POST(HttpRequest.BodyPublishers.ofString(json)) 
                    .build(); 

            HttpResponse<String> response = HttpClientProvider.getClient() 
                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
                return AccountResponse.error(response.statusCode(), error);
            }

            Balance balance = mapper.readValue(response.body(), Balance.class);
            return AccountResponse.balance(balance);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client Error: " + e.getMessage());
            return AccountResponse.error(500, error);
        }
    }


    public AccountResponse withdraw(long accountNumber, TransactionRequest req) {
        try {
            String json = mapper.writeValueAsString(req);

            HttpRequest request = HttpRequest.newBuilder() 
                    .uri(URI.create(ApiConfig.BASE_URL + "/accounts/" + accountNumber + "/withdraw"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClientProvider.getClient() 
                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
                return AccountResponse.error(response.statusCode(), error);
            }

            Balance balance = mapper.readValue(response.body(), Balance.class);
            return AccountResponse.balance(balance);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Client Error: ");
            return AccountResponse.error(500, error);
        }
    }


    public List<AccountResponse> listMiniStatement(long accountNumber, String pin, int limit) {
            List<AccountResponse> res = new ArrayList<>();
        try {
            StatementRequest req = new StatementRequest(pin, limit);
            String json = mapper.writeValueAsString(req);

            HttpRequest request = HttpRequest.newBuilder() 
                .uri(URI.create(ApiConfig.BASE_URL + "/accounts/" + accountNumber + "/transactions"))
                .header("Content-Type", "application/json") 
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = HttpClientProvider.getClient() 
                        .send(request, HttpResponse.BodyHandlers.ofString()); 

            if (response.statusCode() != 200) {
                ErrorResponse error = mapper.readValue(response.body(), ErrorResponse.class);
                res.add(AccountResponse.error(response.statusCode(), error));
                return res;
            }

            List<MiniStatement> statements = mapper.readValue(response.body(),
                                                new TypeReference <List<MiniStatement>>() {});   
                                        
            for (MiniStatement statement: statements) {
                res.add(AccountResponse.miniStatement(statement));
            }             
            return res;                                   
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("ClienError: " + e.getMessage());
            res.add(AccountResponse.error(500, error));
            return res;
        }
    }
}
