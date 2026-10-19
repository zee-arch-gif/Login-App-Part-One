package com.mycompany.loginapppartone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Part 2 - QuickChat message feature.
 *
 * Each Message holds a unique ten-digit ID, its running number, the
 * recipient's cell number, the message text and an auto-generated hash.
 * Sent / stored messages are tracked in static lists so the program can
 * report on every message handled while it is running.
 *
 * References:
 * Oracle, 2025. Java Platform, Standard Edition Documentation. [online] Oracle.
 * Available at: https://docs.oracle.com/en/java/javase/
 * Oracle, 2025. Class String. Java Platform SE API Specification.
 * Available at: https://docs.oracle.com/en/java/javase/
 * JSON.org, n.d. Introducing JSON. Available at: https://www.json.org/
 */
public class Message {

    public static final int MAX_MESSAGE_LENGTH = 250;
    public static final int MAX_ID_LENGTH = 10;

    /** Default file the stored (send-later) messages are written to. */
    public static final String DEFAULT_STORE_FILE = "messages.json";

    private static final SecureRandom RANDOM = new SecureRandom();

    // Messages sent / stored while the program is running.
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();

    private final String messageID;
    private final int numMessagesSent;
    private final String recipient;
    private final String message;
    private final String messageHash;

    /**
     * Creates a message with an auto-generated ten-digit ID.
     *
     * @param numMessagesSent the running message number (starts at 0)
     */
    public Message(int numMessagesSent, String recipient, String message) {
        this(generateMessageID(), numMessagesSent, recipient, message);
    }

    /**
     * Creates a message with a known ID (used by the unit tests so the
     * auto-generated hash can be predicted).
     */
    public Message(String messageID, int numMessagesSent, String recipient, String message) {
        this.messageID = messageID;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // ------------------------------------------------------------------
    // Required methods
    // ------------------------------------------------------------------

    /** Randomly generates a ten-digit ID (leading zeros are allowed). */
    public static String generateMessageID() {
        long value = (long) (RANDOM.nextDouble() * 10_000_000_000L);
        return String.format("%010d", value);
    }

    /** Ensures the message ID is not more than ten characters (and numeric). */
    public boolean checkMessageID() {
        return messageID != null
                && !messageID.isEmpty()
                && messageID.length() <= MAX_ID_LENGTH
                && messageID.chars().allMatch(Character::isDigit);
    }

    /** Feedback used when an ID has been created. */
    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
    }

    /**
     * Ensures the recipient number starts with an international code (+)
     * and is made up of digits only.
     *
     * NOTE: the brief says "no more than ten characters", but its own test
     * data (+27718693002) is 12 characters, so the national part is limited
     * to ten digits after a 1-3 digit country code (max 13 digits after +).
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+\\d{9,13}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    /** True when checkRecipientCell() reports success. */
    public boolean isRecipientValid() {
        return checkRecipientCell().equals("Cell phone number successfully captured.");
    }

    /** Checks the 250-character limit. */
    public String checkMessageLength() {
        int length = message == null ? 0 : message.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - MAX_MESSAGE_LENGTH)
                + ", please reduce the size.";
    }

    /**
     * Hash = first two digits of the ID : message number : first word + last
     * word of the message, in capitals. e.g. 00:0:HITHANKS
     */
    public String createMessageHash() {
        String id = messageID == null ? "" : messageID;
        String prefix = id.length() >= 2 ? id.substring(0, 2) : id;

        String text = message == null ? "" : message.trim();
        String[] words = text.isEmpty() ? new String[0] : text.split("\\s+");

        String first = words.length > 0 ? stripPunctuation(words[0]) : "";
        String last = words.length > 0 ? stripPunctuation(words[words.length - 1]) : "";

        return (prefix + ":" + numMessagesSent + ":" + first + last).toUpperCase();
    }

    /** Removes anything that is not a letter or digit from the ends of a word. */
    private static String stripPunctuation(String word) {
        return word.replaceAll("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$", "");
    }

    /**
     * Lets the user choose to send, disregard or store the message.
     * Prompts on the supplied Scanner.
     */
    public String sentMessage(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            choice = -1;
        }
        return sentMessage(choice);
    }

    /**
     * Applies the user's choice (1 = send, 2 = disregard, 3 = store) and
     * returns the feedback message.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option. Please choose 1, 2 or 3.";
        }
    }

    /** Returns the full details of every message sent so far. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.toDisplayString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /** Total number of messages sent. */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    /**
     * Stores this message in the JSON file (research: hand-built JSON so no
     * external library is required).
     */
    public void storeMessage() {
        storeMessage(Paths.get(DEFAULT_STORE_FILE));
    }

    public void storeMessage(Path file) {
        storedMessages.add(this);
        try {
            Files.writeString(file, storedMessagesAsJson(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not write to " + file + ": " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /** Details in the required order: ID, hash, recipient, message. */
    public String toDisplayString() {
        return "Message ID: " + messageID + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + message + System.lineSeparator();
    }

    public String toJson() {
        return "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"messageNumber\": " + numMessagesSent + ",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(message) + "\"\n"
                + "  }";
    }

    public static String storedMessagesAsJson() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(storedMessages.get(i).toJson());
            sb.append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        return sb.append("]\n").toString();
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /** Clears all tracked messages (used between unit tests). */
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
    }

    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
}
