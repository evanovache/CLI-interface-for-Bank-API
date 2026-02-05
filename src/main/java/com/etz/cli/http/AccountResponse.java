package com.etz.cli.http;

import com.etz.dto.Balance;
import com.etz.dto.ErrorResponse;

public class AccountResponse {
    
    private int statusCode;
    private ErrorResponse error;
    private Balance balance;

    public AccountResponse() {}

    private AccountResponse(int statusCode, 
                            ErrorResponse error,
                            Balance balance
                        ) {
        this.statusCode = statusCode;
        this.error = error;
        this.balance = balance;
    }

    public static AccountResponse balance(Balance balance) {
        return new AccountResponse(200, null, balance);
    }

    public static AccountResponse error(int statusCode, ErrorResponse error) {
        return new AccountResponse(statusCode, error, null);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorResponse getError() {
        return error;
    }

    public Balance getBalance() {
        return balance;
    }
}
