
package com.mycompany.loginapppartone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    void setup() {
        Message.resetAll();
    }

    @Test
    void testMessageID() {
        Message m = new Message(
                "0838884567", 4,
                "+27838884567", "It is dinner time!");

        assertTrue(m.checkMessageID());
        assertEquals("0838884567", m.getMessageID());
    }

    @Test
    void testInvalidMessageID() {
        Message m = new Message(
                "12345678901", 1,
                "+27838884567", "Hello");

        assertFalse(m.checkMessageID());
    }

    @Test
    void testRecipientNumber() {
        Message m = new Message(
                1, "+27838884567", "Hello");

        assertTrue(m.isRecipientValid());
    }

    @Test
    void testInvalidRecipient() {
        Message m = new Message(
                1, "0838884567", "Hello");

        assertFalse(m.isRecipientValid());
    }

    @Test
    void testMessageLength() {
        Message m = new Message(
                1, "+27838884567", "Hello");

        assertEquals(
                "Message ready to send.",
                m.checkMessageLength());
    }

    @Test
    void testMessageTooLong() {
        String longText = "A".repeat(251);

        Message m = new Message(
                1, "+27838884567", longText);

        assertTrue(m.checkMessageLength()
                .contains("exceeds 250 characters"));
    }

    @Test
    void testMessageHash() {
        Message m = new Message(
                "0838884567", 4,
                "+27838884567", "It is dinner time!");

        assertEquals(
                "08:4:ITTIME",
                m.getMessageHash());
    }

    @Test
    void testSendMessage() {
        Message m = new Message(
                1, "+27838884567", "Hello");

        assertEquals(
                "Message successfully sent.",
                m.sentMessage(1));

        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void testDisregardMessage() {
        Message m = new Message(
                1, "+27838884567", "Hello");

        assertEquals(
                "Message disregarded.",
                m.sentMessage(2));

        assertEquals(1,
                MessageArrays.getDisregardedMessages().size());
    }

    @Test
    void testStoreMessage() {
        Message m = new Message(
                1, "+27838884567", "Hello");

        assertEquals(
                "Message successfully stored.",
                m.sentMessage(3));

        assertEquals(1,
                MessageArrays.getStoredMessages().size());
    }
}
