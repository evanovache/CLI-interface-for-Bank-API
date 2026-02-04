package com.etz.cli.http;

import com.etz.cli.model.User;
import com.etz.dto.ErrorResponse;

public class ApiResponse {
    
    private final int statusCode;
    private final User data;
    private final ErrorResponse error;

    private ApiResponse(int statusCode, User data, ErrorResponse error) {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
    }

    public static  ApiResponse success(User data) {
        return new ApiResponse (200, data, null);
    }

    public static ApiResponse error(int statusCode, ErrorResponse error) {
        return new ApiResponse(statusCode, null, error);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public User getData() {
        return data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
