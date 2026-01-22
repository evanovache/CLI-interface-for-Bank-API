package com.etz.cli;

import java.util.Scanner;

public class ConsoleMenu {
    
    private final ApiClient apiClient = new ApiClient();
    private final Scanner in = new Scanner(System.in);

    public void start() {
        
        while(true) {
            System.out.println("\n=== APEX BANK ===");
            System.out.println("1. Ping API");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1:
                    pingApi();
                    break;
                case 0:
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
}
