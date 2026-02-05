package com.etz.cli.http;

import com.etz.cli.model.User;
import com.etz.dto.Account;
import com.etz.dto.ErrorResponse;

public class ApiResponse {
    
    private final int statusCode;
    private final User data;
    private final ErrorResponse error;
    private final Account account;

    private ApiResponse(int statusCode, 
                        User data,
                        ErrorResponse error,
                        Account account) 
    {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
        this.account = account;
    }

    public static  ApiResponse user(int statusCode, User data) {
        return new ApiResponse (statusCode, data, null, null);
    }

    public static ApiResponse error(int statusCode, ErrorResponse error) {
        return new ApiResponse(statusCode, null, error, null);
    }

    public static ApiResponse account(Account account) {
        return new ApiResponse(200, null, null, account);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public User getData() {
        return data;
    }
    
    public Account getAccount() {
        return account;
    }

    public ErrorResponse getError() {
        return error;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
