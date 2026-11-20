
package com.mycompany.loginapppartone;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setup() {
        MessageArrays.resetAll();
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
        MessageArrays.resetAll();
    }

    @Test
    void testReadInteger() {
        Scanner scanner = new Scanner("abc\n4\n");
        assertEquals(4, Main.readInt(scanner));
    }

    @Test
    void testStoredMessagesMenu() throws Exception {
        Scanner scanner = new Scanner("6\n7\n");

        Main.runStoredMessagesMenu(
                scanner, "+27834557896");

        String result = output.toString(
                StandardCharsets.UTF_8);

       
assertTrue(result.contains("STORED MESSAGES"));
assertTrue(result.contains("No messages available."));
    }

    @Test
    void testQuickChatQuit() throws Exception {
        Scanner scanner = new Scanner("0\n3\n");

        Main.runQuickChat(scanner, "+27834557896");

        String result = output.toString(
                StandardCharsets.UTF_8);

        assertTrue(result.contains("QUICKCHAT MENU"));
        assertTrue(result.contains("Goodbye"));
    }

    @Test
    void testFourthMenuOption() throws Exception {
        Scanner scanner = new Scanner("0\n4\n7\n3\n");

        Main.runQuickChat(scanner, "+27834557896");

        String result = output.toString(
                StandardCharsets.UTF_8);

        assertTrue(result.contains("4. Stored Messages"));
        assertTrue(result.contains("STORED MESSAGES"));
    }
}
