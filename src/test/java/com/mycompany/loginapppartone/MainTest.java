package com.mycompany.loginapppartone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    /** Runs the whole app with scripted keyboard input and returns the output. */
    private String run(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(out));
            Main.main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        return out.toString();
    }

    @Test
    public void testFullFlowSendAndQuit() {
        Message.resetAll();
        String input = String.join("\n",
                "John", "Smith", "ab_1", "Password1!", "+27838968976", // register
                "ab_1", "Password1!",                                  // login
                "1",                                                   // number of messages
                "1", "+27718693002", "Hi Mike, can you join us for dinner tonight?", "1",
                "2",                                                   // coming soon
                "3") + "\n";
        String output = run(input);
        assertTrue(output.contains("Welcome to QuickChat."));
        assertTrue(output.contains("Message successfully sent."));
        assertTrue(output.contains("Coming Soon."));
        assertTrue(output.contains("Total messages sent: 1"));
    }

    @Test
    public void testFailedLoginBlocksMessaging() {
        String input = String.join("\n",
                "John", "Smith", "ab_1", "Password1!", "+27838968976",
                "wrong", "wrong") + "\n";
        String output = run(input);
        assertFalse(output.contains("Welcome to QuickChat."));
    }
}