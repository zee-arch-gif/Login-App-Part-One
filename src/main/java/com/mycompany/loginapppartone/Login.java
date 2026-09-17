/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapppartone;
import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
/**
 * Oracle, 2025. Java Platform, Standard Edition Documentation. [online] Oracle. Available at: https://docs.oracle.com/en/java/javase/ [Accessed 17 September 2026].
 * Farrell, J. 2018. Programming Logic and Design. 9th edn. Boston, MA: Cengage Learning.
 */
public class Login {
    
    private String username;
    private String password;
    private String cellphoneNumber;
    private String firstName;
    private String lastName;

  
    public Login() {
    }

    /**
     * Convenience constructor used so the welcome-back message can include
     * the user's first and last name, as required by the login feature.
     */
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks that a username contains an underscore and is no more than
     * five characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks that a password meets the required complexity rules:
     * at least eight characters long
     * contains a capital letter
     * contains a number
     * contains a special character
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasDigit && hasSpecialChar;
    }

    /**
     * Checks that a cell phone number contains the South African
     * international country code (+27) followed by a 9-digit number,
     * e.g. +27838968976.
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null) {
            return false;
        }
        String southAfricanCellRegex = "^\\+27\\d{9}$";
        return cellNumber.matches(southAfricanCellRegex);
    }

    /**
     * @return the feedback message describing the registration outcome
     */
    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell number is incorrectly formatted or does not contain "
                    + "an international code; correct the number and try again.";
        }

        // All checks passed - store the user's details.
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellNumber;

        return "User successfully registered.";
    }


    /**
     * Verifies that the supplied username and password match the details
     * captured during registration.
     */
    public boolean loginUser(String username, String password) {
        if (this.username == null || this.password == null) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Returns the correct feedback message for a login attempt.
     */
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsername() {
        return username;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}  
    

