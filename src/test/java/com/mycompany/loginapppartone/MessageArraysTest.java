
package com.mycompany.loginapppartone;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

public class MessageArraysTest {

    @TempDir
    Path tempDirectory;

    private Message first;
    private Message second;
    private Message third;
    private Message fourth;
    private Message fifth;

    @BeforeEach
    void setup() {
        MessageArrays.resetAll();
        MessageArrays.setStoreFile(
                tempDirectory.resolve("messages.json"));

        first = new Message(
                "0000000001", 1,
                "+27834557896",
                "Did you get the cake?");

        second = new Message(
                "0000000002", 2,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        third = new Message(
                "0000000003", 3,
                "+27834484567",
                "Yohoooo, I am at your gate.");

        fourth = new Message(
                "0838884567", 4,
                "+27838884567",
                "It is dinner time!");

        fifth = new Message(
                "0000000005", 5,
                "+27838884567",
                "Ok, I am leaving without you.");

        MessageArrays.addSentMessage(first);
        MessageArrays.addStoredMessage(second);
        MessageArrays.addDisregardedMessage(third);
        MessageArrays.addSentMessage(fourth);
        MessageArrays.addStoredMessage(fifth);
    }

    @Test
    void testSentMessagesArray() {
        assertEquals(
                List.of(
                        "Did you get the cake?",
                        "It is dinner time!"),
                MessageArrays.getSentMessageTexts());
    }

    @Test
    void testDisregardedMessagesArray() {
        assertEquals(
                List.of("Yohoooo, I am at your gate."),
                MessageArrays.getDisregardedMessageTexts());
    }

    @Test
    void testStoredMessagesArray() {
        assertEquals(2,
                MessageArrays.getStoredMessages().size());
    }

    @Test
    void testMessageIDsAndHashes() {
        assertEquals(5,
                MessageArrays.getMessageIDs().size());

        assertEquals(5,
                MessageArrays.getMessageHashes().size());
    }

    @Test
    void testLongestStoredMessage() {
        Message longest = MessageArrays.findLongestMessage();

        assertNotNull(longest);
        assertEquals(second.getMessage(), longest.getMessage());
    }

    @Test
    void testSearchByID() {
        Message result =
                MessageArrays.findById("0838884567");

        assertNotNull(result);
        assertEquals(
                "It is dinner time!",
                result.getMessage());
    }

    @Test
    void testSearchByRecipient() {
        List<Message> results =
                MessageArrays.findByRecipient("+27838884567");

        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(
                m -> m.getMessage().equals(second.getMessage())));
        assertTrue(results.stream().anyMatch(
                m -> m.getMessage().equals(fifth.getMessage())));
    }

    @Test
    void testDeleteByHash() throws IOException {
        String result = MessageArrays.deleteByHash(
                second.getMessageHash());

        assertTrue(result.contains("successfully deleted"));
        assertNull(
                MessageArrays.findById(second.getMessageID()));
    }

    @Test
    void testFullReport() {
        String report = MessageArrays.displayReport();

        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
        assertTrue(report.contains("Did you get the cake?"));
    }

    @Test
    void testJSONStorage() throws IOException {
        MessageArrays.saveStoredMessagesToFile();

        MessageArrays.resetAll();
        MessageArrays.setStoreFile(
                tempDirectory.resolve("messages.json"));

        assertEquals(2,
                MessageArrays.loadStoredMessagesFromFile());

        assertEquals(2,
                MessageArrays.getStoredMessages().size());
    }

    @Test
    void testNoDuplicateJSONLoading() throws IOException {
        MessageArrays.saveStoredMessagesToFile();

        assertEquals(0,
                MessageArrays.loadStoredMessagesFromFile());

        assertEquals(2,
                MessageArrays.getStoredMessages().size());
    }
}
