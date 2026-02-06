package com.etz.cli.http;

import com.etz.cli.model.MiniStatement;
import com.etz.dto.Balance;

public class AccountResponse {
    
    private int statusCode;
    private ErrorResponse error;
    private Balance balance;
    private MiniStatement miniStatement;

    public AccountResponse() {}

    private AccountResponse(int statusCode, 
                            ErrorResponse error,
                            Balance balance,
                            MiniStatement miniStatement
                        ) {
        this.statusCode = statusCode;
        this.error = error;
        this.balance = balance;
        this.miniStatement = miniStatement;
    }

    public static AccountResponse balance(Balance balance) {
        return new AccountResponse(200, null, balance, null);
    }

    public static AccountResponse error(int statusCode, ErrorResponse error) {
        return new AccountResponse(statusCode, error, null, null);
    }

    public static AccountResponse miniStatement(MiniStatement miniStatement) {
        return new AccountResponse(200, null, null, miniStatement);
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

    public MiniStatement getMiniStatement() {
        return miniStatement;
    }
}
