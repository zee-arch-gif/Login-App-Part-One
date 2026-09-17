/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.loginapppartone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
/**
 * JUnit Team, 2025. JUnit 5 User Guide. [online] JUnit. Available at: https://docs.junit.org/current/user-guide/ [Accessed 17 September 2026].
 */
public class LoginTest {

    public LoginTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test 1: Valid username.
     */
    @Test
    public void testCheckUserNameValid() {
        Login instance = new Login();

        String username = "ab_1";

        assertTrue(instance.checkUserName(username));
    }

    /**
     * Test 2: Invalid username.
     */
    @Test
    public void testCheckUserNameInvalid() {
        Login instance = new Login();

        String username = "abcde";

        assertFalse(instance.checkUserName(username));
    }

    /**
     * Test 3: Valid password.
     */
    @Test
    public void testCheckPasswordComplexityValid() {
        Login instance = new Login();

        String password = "Password1!";

        assertTrue(instance.checkPasswordComplexity(password));
    }

    /**
     * Test 4: Invalid password.
     */
    @Test
    public void testCheckPasswordComplexityInvalid() {
        Login instance = new Login();

        String password = "password";

        assertFalse(instance.checkPasswordComplexity(password));
    }

    /**
     * Test 5: Valid South African cellphone number.
     */
    @Test
    public void testCheckCellPhoneNumberValid() {
        Login instance = new Login();

        String cellNumber = "+27838968976";

        assertTrue(instance.checkCellPhoneNumber(cellNumber));
    }

    /**
     * Test 6: Invalid cellphone number.
     */
    @Test
    public void testCheckCellPhoneNumberInvalid() {
        Login instance = new Login();

        String cellNumber = "0838968976";

        assertFalse(instance.checkCellPhoneNumber(cellNumber));
    }

    /**
     * Test 7: Invalid registration because username is incorrect.
     */
    @Test
    public void testRegisterUserInvalidUsername() {
        Login instance = new Login();

        String username = "";
        String password = "Password1!";
        String cellNumber = "+27838968976";

        String expected =
                "Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.";

        String result = instance.registerUser(
                username,
                password,
                cellNumber
        );

        assertEquals(expected, result);
    }

    /**
     * Test 8: Invalid registration because password is incorrect.
     */
    @Test
    public void testRegisterUserInvalidPassword() {
        Login instance = new Login();

        String username = "ab_1";
        String password = "password";
        String cellNumber = "+27838968976";

        String expected =
                "Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a "
                + "capital letter, a number, and a special character.";

        String result = instance.registerUser(
                username,
                password,
                cellNumber
        );

        assertEquals(expected, result);
    }

    /**
     * Test 9: Successful registration.
     */
    @Test
    public void testRegisterUserValid() {
        Login instance = new Login();

        String username = "ab_1";
        String password = "Password1!";
        String cellNumber = "+27838968976";

        String expected = "User successfully registered.";

        String result = instance.registerUser(
                username,
                password,
                cellNumber
        );

        assertEquals(expected, result);
    }

    /**
     * Test 10: Login should fail when incorrect credentials are supplied.
     */
    @Test
    public void testLoginUserInvalid() {
        Login instance = new Login();

        instance.registerUser(
                "ab_1",
                "Password1!",
                "+27838968976"
        );

        boolean result = instance.loginUser(
                "wrong",
                "wrong"
        );

        assertFalse(result);
    }

    /**
     * Test 11: Login should succeed with correct credentials.
     */
    @Test
    public void testLoginUserValid() {
        Login instance = new Login();

        instance.registerUser(
                "ab_1",
                "Password1!",
                "+27838968976"
        );

        boolean result = instance.loginUser(
                "ab_1",
                "Password1!"
        );

        assertTrue(result);
    }

    /**
     * Test 12: Failed login status message.
     */
    @Test
    public void testReturnLoginStatusFailed() {
        Login instance = new Login();

        boolean loginSuccess = false;

        String expected =
                "Username or password incorrect, please try again.";

        String result = instance.returnLoginStatus(loginSuccess);

        assertEquals(expected, result);
    }

    /**
     * Test 13: Successful login status message.
     */
    @Test
    public void testReturnLoginStatusSuccessful() {
        Login instance = new Login("John", "Smith");

        boolean loginSuccess = true;

        String expected =
                "Welcome John, Smith it is great to see you again.";

        String result = instance.returnLoginStatus(loginSuccess);

        assertEquals(expected, result);
    }

    /**
     * Test 14: Getters and setters for names.
     */
    @Test
    public void testNameGettersAndSetters() {
        Login instance = new Login();

        instance.setFirstName("John");
        instance.setLastName("Smith");

        assertEquals("John", instance.getFirstName());
        assertEquals("Smith", instance.getLastName());
    }
}