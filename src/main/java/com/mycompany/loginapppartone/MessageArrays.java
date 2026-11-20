
package com.mycompany.loginapppartone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MessageArrays {

    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();

    private static Path storeFile = Paths.get(Message.DEFAULT_STORE_FILE);
    private static final Gson GSON = new Gson();

    private MessageArrays() {}

    public static void setStoreFile(Path path) {
        storeFile = path;
    }

    private static void addUnique(List<Message> list, Message message) {
        if (message == null) {
            return;
        }
        for (Message existing : list) {
            if (existing.getMessageID().equals(message.getMessageID())) {
                return;
            }
        }
        list.add(message);
        if (!messageIDs.contains(message.getMessageID())) {
            messageIDs.add(message.getMessageID());
            messageHashes.add(message.getMessageHash());
        }
    }

    public static void addSentMessage(Message message) {
        addUnique(sentMessages, message);
    }

    public static void addDisregardedMessage(Message message) {
        addUnique(disregardedMessages, message);
    }

    public static void addStoredMessage(Message message) {
        addUnique(storedMessages, message);
    }

    public static List<Message> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    public static List<Message> getDisregardedMessages() {
        return Collections.unmodifiableList(disregardedMessages);
    }

    public static List<Message> getStoredMessages() {
        return Collections.unmodifiableList(storedMessages);
    }

    public static List<String> getMessageIDs() {
        return Collections.unmodifiableList(messageIDs);
    }

    public static List<String> getMessageHashes() {
        return Collections.unmodifiableList(messageHashes);
    }

    private static List<String> texts(List<Message> source) {
        List<String> result = new ArrayList<>();
        for (Message m : source) {
            result.add(m.getMessage());
        }
        return result;
    }

    public static List<String> getSentMessageTexts() {
        return texts(sentMessages);
    }

    public static List<String> getDisregardedMessageTexts() {
        return texts(disregardedMessages);
    }

    public static List<String> getStoredMessageTexts() {
        return texts(storedMessages);
    }

    public static List<Message> allActiveMessages() {
        List<Message> result = new ArrayList<>(sentMessages);
        for (Message m : storedMessages) {
            boolean found = false;
            for (Message existing : result) {
                if (existing.getMessageID().equals(m.getMessageID())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(m);
            }
        }
        return result;
    }

    public static String displaySendersAndRecipients(String sender) {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        StringBuilder result = new StringBuilder();
        for (Message m : storedMessages) {
            result.append("Sender: ").append(sender)
                  .append("\nRecipient: ").append(m.getRecipient())
                  .append("\n\n");
        }
        return result.toString();
    }

    public static Message findLongestMessage() {
        Message longest = null;
        for (Message m : storedMessages) {
            if (longest == null ||
                m.getMessage().length() > longest.getMessage().length()) {
                longest = m;
            }
        }
        return longest;
    }

    public static Message findById(String id) {
        for (Message m : allActiveMessages()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public static List<Message> findByRecipient(String recipient) {
        List<Message> result = new ArrayList<>();
        for (Message m : allActiveMessages()) {
            if (m.getRecipient().equals(recipient)) {
                result.add(m);
            }
        }
        return result;
    }

    public static String deleteByHash(String hash) throws IOException {
        for (Message m : allActiveMessages()) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                int storedIndex = storedMessages.indexOf(m);

                storedMessages.remove(m);
                sentMessages.remove(m);

                if (storedIndex >= 0) {
                    try {
                        saveStoredMessagesToFile();
                    } catch (IOException ex) {
                        storedMessages.add(storedIndex, m);
                        throw ex;
                    }
                }

                messageIDs.remove(m.getMessageID());
                messageHashes.remove(m.getMessageHash());

                return "Message: \"" + m.getMessage()
                        + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        StringBuilder result = new StringBuilder(
                "===== FULL MESSAGE REPORT =====\n");

        List<Message> messages = allActiveMessages();
        if (messages.isEmpty()) {
            return "No messages available.";
        }

        for (Message m : messages) {
            result.append("Message ID: ")
                  .append(m.getMessageID())
                  .append("\nMessage Hash: ")
                  .append(m.getMessageHash())
                  .append("\nRecipient: ")
                  .append(m.getRecipient())
                  .append("\nMessage: ")
                  .append(m.getMessage())
                  .append("\n\n");
        }
        return result.toString();
    }

    public static void saveStoredMessagesToFile() throws IOException {
        Path parent = storeFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(storeFile,
                GSON.toJson(storedMessages),
                StandardCharsets.UTF_8);
    }

    public static int loadStoredMessagesFromFile() throws IOException {
        if (!Files.exists(storeFile)) {
            return 0;
        }

        String json = Files.readString(
                storeFile, StandardCharsets.UTF_8);

        Type type = new TypeToken<List<Message>>() {}.getType();
        List<Message> loaded;

        try {
            loaded = GSON.fromJson(json, type);
        } catch (RuntimeException ex) {
            throw new IOException("Invalid JSON data.", ex);
        }

        int added = 0;
        if (loaded != null) {
            for (Message m : loaded) {
                if (m != null && m.getMessageID() != null
                        && findById(m.getMessageID()) == null) {
                    addStoredMessage(m);
                    added++;
                }
            }
        }
        return added;
    }

    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        storeFile = Paths.get(Message.DEFAULT_STORE_FILE);
    }
}
