
package com.mycompany.loginapppartone;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        Login login = new Login(firstName, lastName);

        System.out.print("Create username: ");
        String username = scanner.nextLine();

        System.out.print("Create password: ");
        String password = scanner.nextLine();

        System.out.print("Enter cellphone (+27...): ");
        String cellphone = scanner.nextLine();

        String registration = login.registerUser(
                username, password, cellphone);
        System.out.println(registration);

        if (!registration.equals("User successfully registered.")) {
            return;
        }

        System.out.print("Login username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Login password: ");
        String enteredPassword = scanner.nextLine();

        boolean success = login.loginUser(
                enteredUsername, enteredPassword);

        System.out.println(login.returnLoginStatus(success));

        if (!success) {
            return;
        }

        try {
            MessageArrays.loadStoredMessagesFromFile();
            runQuickChat(scanner, cellphone);
        } catch (IOException ex) {
            System.out.println("Storage error: " + ex.getMessage());
        }
    }

    static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static void runQuickChat(Scanner scanner, String sender)
            throws IOException {

        System.out.println("\nWelcome to QuickChat!");
        System.out.print("How many messages do you wish to enter? ");

        int limit = readInt(scanner);
        while (limit < 0) {
            System.out.print("Enter zero or a positive number: ");
            limit = readInt(scanner);
        }

        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            int option = readInt(scanner);

            switch (option) {
                case 1:
                    if (entered >= limit) {
                        System.out.println("Message limit reached.");
                        break;
                    }

                    System.out.print("Recipient (+27...): ");
                    String recipient = scanner.nextLine();

                    System.out.print("Enter message: ");
                    String text = scanner.nextLine();

                    Message message = new Message(
                            entered + 1, recipient, text);

                    if (!message.checkMessageID()) {
                        System.out.println("Invalid message ID.");
                        break;
                    }

                    if (!message.isRecipientValid()) {
                        System.out.println(
                                "Invalid recipient cellphone number.");
                        break;
                    }

                    if (text.isBlank() ||
                            text.length() > Message.MAX_MESSAGE_LENGTH) {
                        System.out.println(message.checkMessageLength());
                        break;
                    }

                    System.out.println(message.toDisplayString());

                    System.out.println("\n1. Send Message");
                    System.out.println("2. Disregard Message");
                    System.out.println("3. Store Message");
                    System.out.print("Select: ");

                    int action = readInt(scanner);

                    if (action >= 1 && action <= 3) {
                        System.out.println(message.sentMessage(action));

                        if (action == 3) {
                            MessageArrays.saveStoredMessagesToFile();
                        }

                        entered++;
                    } else {
                        System.out.println("Invalid action.");
                    }
                    break;

                case 2:
                    System.out.println(Message.printMessages());
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                case 4:
                    runStoredMessagesMenu(scanner, sender);
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void runStoredMessagesMenu(
            Scanner scanner, String sender) throws IOException {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("1. Display Sender and Recipient");
            System.out.println("2. Display Longest Stored Message");
            System.out.println("3. Search by Message ID");
            System.out.println("4. Search by Recipient");
            System.out.println("5. Delete Message by Hash");
            System.out.println("6. Display Full Message Report");
            System.out.println("7. Return to Main Menu");
            System.out.print("Select: ");

            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    System.out.println(
                            MessageArrays.displaySendersAndRecipients(sender));
                    break;

                case 2:
                    Message longest = MessageArrays.findLongestMessage();
                    System.out.println(longest == null
                            ? "No stored messages."
                            : longest.toDisplayString());
                    break;

                case 3:
                    System.out.print("Enter Message ID: ");
                    String id = scanner.nextLine();

                    Message found = MessageArrays.findById(id);
                    if (found == null) {
                        System.out.println("Message not found.");
                    } else {
                        System.out.println(
                                "Recipient: " + found.getRecipient());
                        System.out.println(
                                "Message: " + found.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter recipient: ");
                    String recipient = scanner.nextLine();

                    List<Message> matches =
                            MessageArrays.findByRecipient(recipient);

                    if (matches.isEmpty()) {
                        System.out.println("No messages found.");
                    } else {
                        for (Message m : matches) {
                            System.out.println(m.toDisplayString());
                        }
                    }
                    break;

                case 5:
                    System.out.print("Enter message hash: ");
                    String hash = scanner.nextLine();

                    System.out.println(
                            MessageArrays.deleteByHash(hash));
                    break;

                case 6:
                    System.out.println(MessageArrays.displayReport());
                    break;

                case 7:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
