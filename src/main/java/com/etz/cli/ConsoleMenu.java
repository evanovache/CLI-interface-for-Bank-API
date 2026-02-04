package com.etz.cli;

import java.util.Scanner;

import com.etz.cli.client.AuthClient;
import com.etz.cli.client.HealthTest;
import com.etz.cli.http.ApiResponse;
import com.etz.cli.model.User;
import com.etz.dto.ErrorResponse;

public class ConsoleMenu {
    
    private final AuthClient authClient = new AuthClient();
    private final HealthTest hClient = new HealthTest();
    private final Scanner in = new Scanner(System.in);

    public void start() throws Exception {
        
        while(true) {
            System.out.println("\n=== APEX BANK ===");
            System.out.println("1. Ping");
            System.out.println("2. Login");
            System.out.println("3. Sign Up");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1 -> {
                    pingApi();
                    enter();
                }

                case 2 -> {
                    login();
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

    private void login() throws Exception {
        System.out.print("Email: ");
        String email = in.nextLine();

        System.out.print("Password: " );
        String password = in.nextLine();

        ApiResponse response = authClient.login(email,password);

        if (response.isSuccess()) {
            User user = response.getData();
            System.out.println("Welcome " + user.getFullName());
        } else {
            ErrorResponse error = response.getError();
            System.out.println(error.getError());
        }
    }

    private void enter() {
        System.out.print("Press Enter To Continue: ");
        in.nextLine();
    }
}
