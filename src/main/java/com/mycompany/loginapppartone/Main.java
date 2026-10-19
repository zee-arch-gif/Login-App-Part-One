package com.mycompany.loginapppartone;

import java.util.Scanner;

/**
 * Console app: Part 1 (registration and login) followed by
 * Part 2 (QuickChat - sending messages).
 *
 * Oracle, 2025. Scanner (Java Platform SE). Available at:
 * https://docs.oracle.com/en/java/javase/ [Accessed 17 September 2026].
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ---------------- Part 1: registration and login ----------------
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        Login login = new Login(firstName, lastName);

        System.out.print("Choose a username (must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();

        System.out.print("Choose a password (8+ chars, capital, number, special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter your SA cell number (e.g. +27838968976): ");
        String cellNumber = scanner.nextLine();

        String registrationMessage = login.registerUser(username, password, cellNumber);
        System.out.println("\n" + registrationMessage + "\n");

        if (!registrationMessage.equals("User successfully registered.")) {
            System.out.println("Registration failed - exiting.");
            scanner.close();
            return;
        }

        System.out.println("--- Now let's log in ---");

        System.out.print("Username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        System.out.println("\n" + login.returnLoginStatus(loginSuccess));

        // Users may only send messages once they have logged in successfully.
        if (!loginSuccess) {
            scanner.close();
            return;
        }

        // ---------------- Part 2: QuickChat ----------------
        runQuickChat(scanner);
        scanner.close();
    }

    /** Menu loop - runs until the user selects Quit. */
    static void runQuickChat(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        int maxMessages = readPositiveInt(scanner,
                "How many messages do you wish to enter? ");
        int messagesEntered = 0;

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered >= maxMessages) {
                        System.out.println("You have already entered the " + maxMessages
                                + " message(s) you set at the start.");
                    } else {
                        while (messagesEntered < maxMessages) {
                            if (captureMessage(scanner, messagesEntered)) {
                                messagesEntered++;
                            }
                        }
                        System.out.println("\nTotal messages sent: "
                                + Message.returnTotalMessages());
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
    }

    /**
     * Captures one message. Returns true once the message has been dealt with
     * (sent, stored or disregarded) so it counts towards the user's limit.
     */
    private static boolean captureMessage(Scanner scanner, int messageNumber) {
        System.out.println("\n--- Message " + (messageNumber + 1) + " ---");

        String recipient;
        while (true) {
            System.out.print("Recipient cell number (with international code, e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            Message check = new Message(messageNumber, recipient, "");
            String result = check.checkRecipientCell();
            System.out.println(result);
            if (check.isRecipientValid()) {
                break;
            }
        }

        String text;
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            text = scanner.nextLine();
            Message check = new Message(messageNumber, recipient, text);
            String result = check.checkMessageLength();
            if (result.equals("Message ready to send.")) {
                System.out.println(result);
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
            System.out.println(result);
        }

        Message msg = new Message(messageNumber, recipient, text);
        System.out.println(msg.messageIDGenerated());

        String feedback = msg.sentMessage(scanner);
        System.out.println(feedback);

        if (feedback.startsWith("Message successfully sent")) {
            System.out.println("\n" + msg.toDisplayString());
        } else if (feedback.startsWith("Press 0")) {
            System.out.print("> ");
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            } else {
                System.out.println("Message disregarded.");
            }
        } else if (feedback.startsWith("Invalid")) {
            return false; // ask again for this message
        }
        return true;
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }
}