package com.dbi;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    Connection connection;
    Scanner scanner;
    Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //open new account
    long openAccount(String email){
        scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double deposit = scanner.nextDouble();
        if (deposit < 0) throw new RuntimeException("Deposit can't be Negative");
        scanner.nextLine();
        System.out.print("Enter 4 digits Security Pin: ");
        String secPin = scanner.nextLine();
        if (secPin.length() > 4) throw new RuntimeException("Security Pin must be of 4 digits only!!!");

        if (!account_exists(email)){
            String query = "INSERT INTO accounts(acc_no, security_pin, balance, email) VALUES (?, ?, ?, ?);";
            try {
                long accNo = accountNoGenerator();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, accNo);
                statement.setString(2, secPin);
                statement.setDouble(3, deposit);
                statement.setString(4, email);

                int x = statement.executeUpdate();
                if (x >= 1){
                    return accNo;
                }
                throw new RuntimeException("Failed to create account!!");
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account Already Exists!!");
    }

    //get account number of a user
    long getAccNo(String email){
        String query = "SELECT acc_no FROM accounts WHERE email = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("acc_no");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number Doesn't Exist!!!");
    }

    //Account number generator
    long accountNoGenerator(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT acc_no FROM accounts ORDER BY acc_no DESC LIMIT 1;");
            if (resultSet.next()){
                return resultSet.getLong("acc_no") + 1;
            }
            return 1333001001;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1333001001;
    }

    //Account exist?
    boolean account_exists(String email){
        String accountExist = "SELECT * FROM accounts WHERE email = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(accountExist);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
}
