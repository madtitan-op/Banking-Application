package com.dbi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //transfer money
    void transferMoney(long senderAcc){
        scanner.nextLine();
        System.out.print("Receiver Account Number: ");
        long receiverAcc = scanner.nextLong();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (senderAcc != 0 && receiverAcc != 0){
                PreparedStatement statement = connection.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ? AND security_pin = ?");
                statement.setLong(1, senderAcc);
                statement.setString(2, pin);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    double senderBalance = resultSet.getDouble("balance");

                    if (amount <= senderBalance){
                        String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";
                        String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debitQuery);
                        PreparedStatement creditStatement = connection.prepareStatement(creditQuery);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, senderAcc);
                        creditStatement.setDouble(1, amount);
                        creditStatement.setLong(2, receiverAcc);
                        if (debitStatement.executeUpdate() >= 1 && creditStatement.executeUpdate() >= 1){
                            System.out.println("Rs. " + amount + " transferred successfully to Account Number: " + receiverAcc);
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else {
                            System.out.println("Transaction failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else System.out.println("Insufficient Balance!!");
                }
                else System.out.println("Invalid PIN!!");
            }
            else System.out.println("Invalid Account number!!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //credit money
    void creditMoney(long accNo){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accNo > 0){
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? AND security_pin = ?;");
                statement.setLong(1, accNo);
                statement.setString(2, pin);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()){
                    PreparedStatement statement1 = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE acc_no = ?;");
                    statement1.setDouble(1, amount);
                    statement1.setLong(2, accNo);
                    int x = statement1.executeUpdate();

                    if (x >= 1){
                        System.out.println("Your Account is credited by Rs. " + amount);
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }

                    System.out.println("Transaction Failed!!!");
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                else System.out.println("Invalid PIN!!!");
            }
            else System.out.println("Invalid account number!!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //debit money
    void debitMoney(long accNo){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accNo > 0){
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? AND security_pin = ?;");
                statement.setLong(1, accNo);
                statement.setString(2, pin);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    double currentBal = resultSet.getDouble("balance");
                    if (currentBal >= amount){
                        PreparedStatement statement1 = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ?;");
                        statement1.setDouble(1, amount);
                        statement1.setLong(2, accNo);
                        int x = statement1.executeUpdate();
                        if (x >= 1){
                            System.out.println("Your Account is debited by Rs. " + amount);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        System.out.println("Transaction Failed!!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                    else System.out.println("Insufficient Balance!!!");
                }
                else System.out.println("Invalid PIN!!!");
            }
            else System.out.println("Invalid Account Number!!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //fetch balance
    void getBalance(long accNo){
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ? AND security_pin = ?");
            statement.setLong(1, accNo);
            statement.setString(2, pin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                System.out.println("Your current balance is: Rs. " + resultSet.getDouble("balance"));
            }
            else System.out.println("Invalid PIN!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

/*
    //transfer money
    void transferMoney(long fromAccNo){
        System.out.print("Transfer to: ");
        long toAccNo = scanner.nextLong();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ? AND security_pin = ?");
            statement.setLong(1, fromAccNo);
            statement.setString(2, pin);
            ResultSet resultSet = statement.executeQuery();
            double senderBalance = 0;
            if (resultSet.next()){
                senderBalance = resultSet.getDouble("balance");
            }
            else System.out.println("Invalid PIN!!!");
            if (senderBalance >= amount){

            }
            else System.out.println("Insufficient Balance!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
*/
