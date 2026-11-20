
package com.mycompany.loginapppartone;

import java.security.SecureRandom;

public class Message {

    public static final int MAX_MESSAGE_LENGTH = 250;
    public static final String DEFAULT_STORE_FILE = "messages.json";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final String messageID;
    private final int numMessagesSent;
    private final String recipient;
    private final String message;
    private final String messageHash;

    public Message(int number, String recipient, String message) {
        this(generateMessageID(), number, recipient, message);
    }

    public Message(String id, int number, String recipient,
                   String message) {
        this.messageID = id;
        this.numMessagesSent = number;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public static String generateMessageID() {
        return String.format("%010d",
                RANDOM.nextLong(10_000_000_000L));
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{1,10}");
    }

    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
    }

    public boolean isRecipientValid() {
        return recipient != null
                && recipient.matches("\\+[1-9]\\d{7,14}");
    }

    public String checkRecipientCell() {
        return isRecipientValid()
                ? "Cell phone number successfully captured."
                : "Cell phone number is incorrectly formatted.";
    }

    public String checkMessageLength() {
        if (message == null) {
            return "Message cannot be empty.";
        }

        if (message.length() > MAX_MESSAGE_LENGTH) {
            return "Message exceeds 250 characters by "
                    + (message.length() - MAX_MESSAGE_LENGTH)
                    + ", please reduce size.";
        }

        return "Message ready to send.";
    }

    public String createMessageHash() {
        if (message == null || message.isBlank()) {
            return messageID + ":" + numMessagesSent + ":EMPTY";
        }

        String[] words = message.trim().split("\\s+");

        String first = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String last = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        String prefix = messageID.length() >= 2
                ? messageID.substring(0, 2)
                : messageID;

        return (prefix + ":" + numMessagesSent + ":"
                + first + last).toUpperCase();
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                MessageArrays.addSentMessage(this);
                return "Message successfully sent.";

            case 2:
                MessageArrays.addDisregardedMessage(this);
                return "Message disregarded.";

            case 3:
                MessageArrays.addStoredMessage(this);
                return "Message successfully stored.";

            default:
                return "Invalid choice.";
        }
    }

    public static String printMessages() {
        StringBuilder result = new StringBuilder();

        for (Message m : MessageArrays.getSentMessages()) {
            result.append(m.toDisplayString()).append("\n");
        }

        return result.length() == 0
                ? "No sent messages."
                : result.toString();
    }

    public static int returnTotalMessages() {
        return MessageArrays.getSentMessages().size();
    }

    public String toDisplayString() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    public static void resetAll() {
        MessageArrays.resetAll();
    }

    public String getMessageID() {
        return messageID;
    }

    public int getNumMessagesSent() {
        return numMessagesSent;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
