package com.etz.cli.model;

import java.time.LocalDateTime;

public class MiniStatement {
    
    private long accountNumber;
    private int transactionId;
    private double amount;
    private LocalDateTime timeOfTransaction;
    private TransactionType transactionType;

    public MiniStatement() {}

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    } 

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(LocalDateTime timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
