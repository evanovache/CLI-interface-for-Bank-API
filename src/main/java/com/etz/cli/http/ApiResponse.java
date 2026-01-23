package com.etz.cli.http;

public class ApiResponse<T> {
    
    private final int statusCode;
    private final T data;
    private final String error;

    private ApiResponse(int statusCode, T data, String error) {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data, null);
    }

    public static <T> ApiResponse<T> error(int statusCode, String error) {
        return new ApiResponse<>(statusCode, null, error);
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public int getStatuscode() {
        return statusCode;
    }
}
