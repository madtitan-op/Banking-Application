package com.dbi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    Connection connection;
    Scanner scanner;

    //CONSTRUCTOR
    User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //Register
    boolean register(){
        //take details for registration
        scanner.nextLine();
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        //check if any of the field is null or empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            System.out.println("Invalid input!! \nTry again!");
            return false;
        }

        //check if user already exists
        if(user_exists(email)){
            System.out.println("User already exists! \nPlease login!");
            return false;
        }

        //query to inserting the data into DB
        String regQuery = "INSERT INTO users(email, password, full_name) VALUES(?, ?, ?);";

        //perform insertion
        try {
            PreparedStatement statement = connection.prepareStatement(regQuery);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, name);

            if (statement.executeUpdate() >= 1){
                System.out.println("Registration Successful!");
                return true;
            }
        }catch (SQLException e){
            System.out.println("Registration Unsuccessful!");
            System.out.println(e.getMessage());
        }
        return true;
    }

    //Login
    public String login(){
        //collect login details from users
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        //query to validate user
        String loginValidator = "SELECT * FROM users WHERE email = ? AND password = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(loginValidator);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                System.out.println("Login successful");
                return email;
            }
            return null;
        }catch (SQLException e){
            System.out.println("Login failed! \n Exception occurred!!");
            System.out.println(e.getMessage());
        }
        return null;
    }
    //User exist?
    boolean user_exists(String email){
        String userExist = "SELECT * FROM users WHERE email = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(userExist);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
}
