package com.etz.cli;

import java.util.Scanner;

public class ConsoleMenu {
    
    private final ApiClient apiClient = new ApiClient();
    private final Scanner in = new Scanner(System.in);

    public void start() {
        
        while(true) {
            System.out.println("\n=== APEX BANK ===");
            System.out.println("1. Ping");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1:
                    pingApi();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Goodbye!!");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void pingApi() {
        String response = apiClient.ping();
        System.out.println("Response from API: ");
        System.out.println(response);
    }

    private void login() {
        System.out.print("Email: ");
        String email = in.nextLine();

        System.out.print("Password: " );
        String password = in.nextLine();

        ApiResponse response = apiClient.login(email,password);

        if (response.getStatusCode() == 200) {
            System.out.println("Login Successful");
            System.out.println("User data: ");
            System.out.println(response.getBody());
        } else {
            System.out.println("Login failed");
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Message: " + response.getBody());
        }
    }
}
