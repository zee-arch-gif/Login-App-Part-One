/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loginapppartone;
import java.util.Scanner;

/**
 *
 * @author Student
 */
/**
 * Demo console app for Part 1 - Registration and login feature
 */
public class Main {
   public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        Login login = new Login(firstName, lastName);

        System.out.print("Choose a username (must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();

        System.out.print("Choose a password (8+ chars, capital, number, special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter your SA cell number (e.g. +27838968976): ");
        String cellNumber = scanner.nextLine();

        String registrationMessage = login.registerUser(username, password, cellNumber);
        System.out.println("\n" + registrationMessage + "\n");

        if (!registrationMessage.equals("User successfully registered.")) {
            System.out.println("Registration failed - exiting.");
            scanner.close();
            return;
        }

        System.out.println("--- Now let's log in ---");

        System.out.print("Username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        System.out.println("\n" + login.returnLoginStatus(loginSuccess));

        scanner.close();
    }
}