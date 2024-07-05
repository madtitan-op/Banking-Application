package com.dbi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    //DB credentials
    private static final String url = "jdbc:mysql://localhost:3306/banking";
    private static final String username = "root";
    private static final String password = "password";

    public static void main(String[] args) {
        //Load Drivers
        try {
            Class.forName("com.mysql.cj.jdbc.Drivers");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            //Initialize connection with the database
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);

            //Initializing required objects
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long accNo;

            while (true){
                System.out.println("\n-----WELCOME TO BANKING SYSTEM-----\n");
                System.out.print("1. Register \n2. Login \n3. Exit \nENTER YOUR CHOICE: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null){
                            System.out.println("User logged in!");
                            if (!accounts.account_exists(email)){
                                System.out.print("\n1. OPEN NEW ACCOUNT \n2. EXIT \nENTER YOUR CHOICE: ");
                                int choice1 = scanner.nextInt();
                                if (choice1 == 1){
                                    accNo = accounts.openAccount(email);
                                    System.out.println("Account created successfully!");
                                }
                                else break;
                            }
                            else accNo = accounts.getAccNo(email);
                            int choice2 = 0;
                            while (choice2 != 5){
                                System.out.print("\n1. Debit Money \n2. Credit Money \n3. Transfer Money \n4. Get Balance \n5. EXIT \nENTER YOUR CHOICE: ");
                                choice2 = scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debitMoney(accNo);
                                        break;
                                    case 2:
                                        accountManager.creditMoney(accNo);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(accNo);
                                        break;
                                    case 4:
                                        accountManager.getBalance(accNo);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice!");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("Incorrect email or password!!");
                        }
                        break;
                    case 3:
                        System.out.println("-----THANK YOU FOR USING OUR BANKING SYSTEM-----");
                        System.out.println("Exiting System!");
                        break;
                    default:
                        System.out.println("Enter a valid choice!!");
                }
                if (choice == 3) break;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
