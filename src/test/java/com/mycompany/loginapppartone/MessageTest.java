package com.mycompany.loginapppartone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class using the test data from the brief.
 *
 * JUnit Team, 2025. JUnit 5 User Guide. [online] JUnit. Available at:
 * https://docs.junit.org/current/user-guide/ [Accessed 17 September 2026].
 */
public class MessageTest {

    // Test data for Message 1 and Message 2
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    public void setUp() {
        Message.resetAll();
    }

    // ---- Message length ----

    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(260);
        Message m = new Message("0012345678", 0, RECIPIENT_1, longText);
        assertEquals("Message exceeds 250 characters by 10, please reduce the size.",
                m.checkMessageLength());
    }

    // ---- Recipient number ----

    @Test
    public void testRecipientSuccess() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure() {
        Message m = new Message("0012345678", 1, RECIPIENT_2, TEXT_2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                m.checkRecipientCell());
    }

    // ---- Message hash ----

    @Test
    public void testMessageHashTestCase1() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    public void testMessageHashLoop() {
        String[][] data = {
            {"0012345678", "0", RECIPIENT_1, TEXT_1, "00:0:HITONIGHT"},
            {"0112345678", "1", RECIPIENT_2, TEXT_2, "01:1:HIPAYMENT"},
            {"9912345678", "2", RECIPIENT_1, "Thanks", "99:2:THANKSTHANKS"},
            {"1234567890", "3", RECIPIENT_1, "Hi thanks!", "12:3:HITHANKS"}
        };
        for (String[] row : data) {
            Message m = new Message(row[0], Integer.parseInt(row[1]), row[2], row[3]);
            assertEquals(row[4], m.createMessageHash());
        }
    }

    // ---- Message ID ----

    @Test
    public void testMessageIDCreated() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.checkMessageID());
        assertEquals("Message ID generated: " + m.getMessageID(), m.messageIDGenerated());
    }

    @Test
    public void testMessageIDTooLong() {
        Message m = new Message("12345678901", 0, RECIPIENT_1, TEXT_1);
        assertFalse(m.checkMessageID());
    }

    // ---- Send / disregard / store ----

    @Test
    public void testSendMessage() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        Message m = new Message("0012345678", 1, RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    public void testStoreMessage() throws Exception {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        java.nio.file.Path tmp = java.nio.file.Files.createTempFile("messages", ".json");
        m.storeMessage(tmp);
        String json = java.nio.file.Files.readString(tmp);
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        java.nio.file.Files.deleteIfExists(tmp);
    }

    // ---- Totals ----

    @Test
    public void testReturnTotalMessages() {
        Message m1 = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        Message m2 = new Message("0112345678", 1, RECIPIENT_2, TEXT_2);
        m1.sentMessage(1); // send
        m2.sentMessage(2); // discard
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages() {
        Message m1 = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        m1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("0012345678"));
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains(RECIPIENT_1));
        assertTrue(printed.contains(TEXT_1));
    }
}