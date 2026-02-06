package com.etz.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.etz.cli.client.AccountClient;
import com.etz.cli.client.AuthClient;
import com.etz.cli.client.HealthTest;
import com.etz.cli.client.UserClient;
import com.etz.cli.http.AccountResponse;
import com.etz.cli.http.ApiResponse;
import com.etz.cli.http.ErrorResponse;
import com.etz.cli.model.AccountType;
import com.etz.cli.model.MiniStatement;
import com.etz.cli.model.User;
import com.etz.dto.Account;
import com.etz.dto.Balance;
import com.etz.dto.CreateAccountRequest;
import com.etz.dto.SignUpRequest;
import com.etz.dto.TransactionRequest;

public class ConsoleMenu {
    
    private final AuthClient authClient = new AuthClient();
    private final UserClient userClient = new UserClient();
    private final AccountClient accountClient = new AccountClient();
    private final HealthTest hClient = new HealthTest();
    private final Scanner in = new Scanner(System.in);

    public void start() throws Exception {
        
        while(true) {
            clearScreen();
            System.out.println("\n=== APEX BANK ===");
            System.out.println("1. Ping");
            System.out.println("2. Login");
            System.out.println("3. Sign Up");
            System.out.println("4. Exit");
            System.out.print("Enter Option: ");

            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1 -> {
                    pingApi();
                    enter();
                }

                case 2 -> {
                    int id = login();
                    if (id == 0) {
                        enter();
                        break;
                    }
                    while (true) {
                        clearScreen();
                        System.out.println("1. View Accounts");
                        System.out.println("2. Create New Account");
                        System.out.println("3. Logout");
                        System.out.print("Enter Option: ");
                        choice = in.nextInt();
                        in.nextLine();
                        switch(choice) {
                            case 1 -> {
                                clearScreen();
                                System.out.println("******YOUR ACCOUNTS*******");
                                List<Account> accounts = listAccounts(id);
                                if (accounts.isEmpty()) {
                                    enter();
                                    break;
                                }
            
                                System.out.print("Choose Account To Manage: "); 
                                choice = in.nextInt();
                                in.nextLine();
                                Account account = accounts.get(choice - 1);
                                long accountNumber = account.getAccountNumber(); 
                                while (true) {
                                    clearScreen();
                                    System.out.print("Account Number: " + accountNumber);
                                    System.out.println("\t\t(" + account.getAccountType() + " ACCOUNT)");
                                    System.out.println("1. Check Balance");
                                    System.out.println("2. Deposit");
                                    System.out.println("3. Withdraw");
                                    System.out.println("4. Mini Statement");
                                    System.out.println("5. Go back");
                                    System.out.print("Enter Option: ");
                                    choice = in.nextInt();
                                    in.nextLine();

                                    switch(choice) {
                                        case 1 -> {
                                            checkBalance(accountNumber);
                                        }

                                        case 2 -> {
                                            deposit(accountNumber);
                                        }

                                        case 3 -> {
                                            withdraw(accountNumber);
                                        }

                                        case 4 -> {
                                            miniStatement(accountNumber);
                                        }

                                        case 5 -> {
                                            
                                        }
                                    }
                                    enter(); 
                                    if (choice == 5) 
                                        break;
                                }                             
                            }

                            case 2 -> {
                                createAccount(id);
                                enter();
                            }

                            case 3 -> {
                                break;
                            }
                        }
                        if (choice == 3) 
                            break;
                    }
                }
                
                case 3 -> {
                    signUp();
                    enter();
                }

                case 4 -> {
                    System.out.println("Goodbye!!");
                    return;
                }
                    
                default -> {
                    System.out.println("Invalid choice");
                    enter();
                }               
            }
        }
    }

    private void pingApi() {
        String response = hClient.ping();
        System.out.println("Response from API: ");
        System.out.println(response);
    }


    private int login() throws Exception {
        System.out.print("Email: ");
        String email = in.nextLine();

        System.out.print("Password: " );
        String password = in.nextLine();

        ApiResponse response = authClient.login(email,password);

        if (response.isSuccess()) {
            User user = response.getData();
            System.out.println("Welcome " + user.getFullName());
            return user.getUserId();
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
            return 0;
        }
    }


    private void signUp() throws Exception {
        SignUpRequest req = new SignUpRequest();
        System.out.print("Enter full name: ");
        req.setFullName(in.nextLine());
        System.out.print("Enter email: ");
        req.setEmail(in.nextLine());
        System.out.print("Enter password: ");
        req.setPassword(in.nextLine());
        
        ApiResponse response = userClient.signUp(req);

        if (response.isSuccess()) {
            User user = response.getData();
            System.out.println("Sign Up Successful.");
            System.out.println("Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }


    private List<Account> listAccounts(int id) {
        List<ApiResponse> response = userClient.listAccounts(id);
        if (response.isEmpty())
            System.out.println("You have no accounts");

        List<Account> accounts = new ArrayList<>();
        
        int count = 1;
        for (ApiResponse a: response) {

            if (!a.isSuccess()) {
                System.out.println("Error: " + a.getError().getError());
                continue;
            }

            Account account = a.getAccount();
            accounts.add(account);
            System.out.println(count++ + " ->\tAccount Number: " + account.getAccountNumber());
            System.out.println("\tAccount Type: " + account.getAccountType());
            System.out.println();
        }
        return accounts;
    }


    private void createAccount(int id) {
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Enter Option: ");
        int choice = in.nextInt();
        in.nextLine();
        AccountType type = switch(choice) {
            case 1 -> AccountType.SAVINGS;
            case 2 -> AccountType.CURRENT;
            default -> {
                throw new IllegalArgumentException("Invalid Choice");
            }
        };

        double initialDeposit;
        System.out.println("Enter Initial Deposit: ");
        do {
            initialDeposit = in.nextDouble();
            in.nextLine();

            if (initialDeposit < 50)
                System.out.println("Amount must be $50 or more");
        } while (initialDeposit < 50);

        System.out.print("Enter a four digit pin: ");
        String pin;
        do {
            pin = in.nextLine();
            if (pin.length() != PINLENGTH) 
                System.out.println("Pin Must be 4 digits!!");
        } while (pin.length() != PINLENGTH); 

        CreateAccountRequest request = new CreateAccountRequest(
                                        type, initialDeposit, pin);
        
        ApiResponse response = userClient.createAccount(request, id);

        if (response.isSuccess()) {
            System.out.println("Account Created Successfully");
            Account account = response.getAccount();
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Account Type: " + account.getBalance());
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }


    private void checkBalance(long accountNumber) {
        System.out.print("Enter PIN: ");
        String pin = in.nextLine();
        AccountResponse response = accountClient.balance(accountNumber, pin);

        if (response.isSuccess()) {
            Balance balance = response.getBalance(); 
            System.out.println("Balance: " + balance.getBalance());
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }


    private void deposit(long accountNumber) {
        System.out.print("Enter amount: ");
        double amount = in.nextDouble();
        in.nextLine();
        System.out.print("Enter PIN: ");
        String pin = in.nextLine(); 
        
        TransactionRequest request = new TransactionRequest(pin, amount);
        AccountResponse response = accountClient.deposit(accountNumber, request);

        if (response.isSuccess()) {
            Balance balance = response.getBalance();
            System.out.println("Deposit Successful.");
            System.out.println("Balance: "+ balance.getBalance());
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }


    private void withdraw(long accountNumber) {
        System.out.print("Enter amount: ");
        double amount = in.nextDouble();
        in.nextLine();
        System.out.print("Enter Pin: ");
        String pin = in.nextLine();

        TransactionRequest request = new TransactionRequest(pin, amount);
        AccountResponse response = accountClient.withdraw(accountNumber, request);

        if (response.isSuccess()) {
            Balance balance = response.getBalance(); 
            System.out.println("Withdraw Successful");
            System.out.println("Balance: " + balance.getBalance()); 
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }


    private void miniStatement(long accountNumber) {
        System.out.print("Enter number of recent transactions you want to view: ");
        int limit = in.nextInt();
        in.nextLine();
        System.out.print("Enter pin: ");
        String pin = in.nextLine();

        List<AccountResponse> response = accountClient.listMiniStatement(accountNumber, pin, limit);
        if(response.isEmpty()) {
            System.out.println("You have no transaction history");
            return;
        }
            
        for (AccountResponse a: response) {
            if (!a.isSuccess()) {
                ErrorResponse error = a.getError();
                System.out.println(error.getError());
                return;
            }

            MiniStatement statement = a.getMiniStatement();
            System.out.println();
            System.out.println("Transaction Id: " + statement.getTransactionId());
            System.out.println("Transaction Type: " + statement.getTransactionType());
            System.out.println("Amount: " + statement.getAmount());
            System.out.println("Time: " + statement.getTimeOfTransaction());
        }
    }


    private void enter() {
        System.out.print("Press Enter To Continue: ");
        in.nextLine();
    }

    public void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    }

    private final int PINLENGTH = 4;
}

