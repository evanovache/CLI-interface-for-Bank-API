package com.etz.dto;

public class StatementRequest {
    
    private String pin;
    private int limit;

    public StatementRequest() {}

    public StatementRequest(String pin, int limit) {
        this.pin = pin;
        this.limit = limit;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
