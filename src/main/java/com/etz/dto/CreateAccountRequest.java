package com.etz.dto;

import com.etz.cli.model.AccountType;

public class CreateAccountRequest {
    
    private AccountType type;
    private double initialDeposit;
    private String pin;

    public CreateAccountRequest() {}

    public  CreateAccountRequest(AccountType type,
                            double initialDeposit, 
                            String pin
                        ) {
        this.type = type;
        this.initialDeposit = initialDeposit;
        this.pin = pin;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
