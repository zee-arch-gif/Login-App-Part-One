Login App Part One

Overview

Login App Part One is a Java console-based application developed to demonstrate user registration and login functionality.

The application allows a user to:

- Enter their first and last name.
- Create a username.
- Create a password.
- Enter a South African cellphone number.
- Validate the registration details.
- Register the user if all requirements are met.
- Log in using the registered username and password.
- Display an appropriate login status message.

The project also includes JUnit 5 unit tests to verify the functionality of the `Login` class.

---

 Technologies Used

- Java
- Apache NetBeans
- JUnit 5
- GitHub
- Java Regular Expressions (Regex)

---

 Project Structure

```text
LoginAppPartOne/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── mycompany/
│   │               └── loginapppartone/
│   │                   ├── Main.java
│   │                   └── Login.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── mycompany/
│                   └── loginapppartone/
│                       └── LoginTest.java







part 2
# LoginAppPartOne – Login & QuickChat

A Java console application that combines two features:

1. **Part 1 – Registration and Login:** a user registers with a username, password and South African cell number, then logs in.
2. **Part 2 – QuickChat:** once logged in, the user can send, discard, or store messages to a recipient.

Built with Maven and tested with JUnit 5. A GitHub Actions workflow runs the tests on every push and pull request to `main`.

---

## Features

### Registration and login (Part 1)
- Prompts for first name, last name, username, password and SA cell number.
- Username must contain an underscore (`_`) and be 5 characters or fewer.
- Password must be at least 8 characters and include a capital letter, a number and a special character.
- Registration must succeed before login is offered.
- Messaging is only available after a successful login.

### QuickChat messaging (Part 2)
- The user chooses at the start how many messages they want to enter.
- Menu options:
  1. **Send Messages**
  2. **Show recently sent messages** (currently prints "Coming Soon.")
  3. **Quit** (prints the total messages sent)
- For each message:
  - **Recipient cell number** must start with `+` followed by 9–13 digits (international format, e.g. `+27718693002`).
  - **Message text** is limited to 250 characters. If it is too long, the app reports by how many characters it exceeds the limit.
  - **Message ID:** a random ten-digit ID is generated.
  - **Message hash:** `first two ID digits : message number : FIRSTWORDLASTWORD` in capitals (e.g. `00:0:HITHANKS`).
  - The user then chooses to **send**, **disregard** (press `0` to delete), or **store** the message to send later.
- Stored messages are written to `messages.json` in the working directory (hand-built JSON, no external library needed).

---

## Project structure

| File | Purpose |
|------|---------|
| `Main.java` | Console entry point: registration, login and the QuickChat menu loop |
| `Message.java` | Message model and logic: ID, recipient and length validation, hash, send/store, JSON output |
| `MainTest.java` | Tests for the full console flow (send and quit; failed login blocks messaging) |
| `MessageTest.java` | Unit tests for message length, recipient, hash, ID, send/disregard/store, totals and printing |
| `pom.xml` | Maven build configuration |
| `maven.yml` | GitHub Actions CI workflow |

All classes are in the package `com.mycompany.loginapppartone`.

### Expected layout

For Maven to find everything, arrange the files like this:

```
LoginAppPartOne/
├── pom.xml
├── .github/
│   └── workflows/
│       └── maven.yml
└── src/
    ├── main/java/com/mycompany/loginapppartone/
    │   ├── Main.java
    │   ├── Message.java
    │   └── Login.java
    └── test/java/com/mycompany/loginapppartone/
        ├── MainTest.java
        └── MessageTest.java
```

> **Note:** `Main.java` uses a `Login` class (`registerUser`, `loginUser`, `returnLoginStatus`), which was not included in the files provided. Make sure `Login.java` is present in the same package, otherwise the project will not compile.

---

## Requirements

- **JDK 25** (set via `maven.compiler.release` in `pom.xml`)
- **Apache Maven 3.x**

Dependencies (test scope only): JUnit Jupiter API, Params and Engine 5.10.3, and OpenTest4J 1.3.0.

---

## Build and run

Build and run the tests:

```bash
mvn clean test
```

Run the application:

```bash
mvn compile exec:java -Dexec.mainClass=com.mycompany.loginapppartone.Main
```

Or compile and run directly:

```bash
mvn compile
java -cp target/classes com.mycompany.loginapppartone.Main
```

---

## Example session

```
Enter your first name: Kyle
Enter your last name: Smith
Choose a username (must contain '_' and be <= 5 chars): kyl_1
Choose a password (8+ chars, capital, number, special char): Ch&&sec@ke99!
Enter your SA cell number (e.g. +27838968976): +27838968976

User successfully registered.

--- Now let's log in ---
Username: kyl_1
Password: Ch&&sec@ke99!

Welcome to QuickChat.
How many messages do you wish to enter? 1

Please choose an option:
1) Send Messages
2) Show recently sent messages
3) Quit
Option: 1
```

*(The first-part prompts and messages above depend on `Login.java`; exact wording may differ.)*

---

## Testing

Tests live in `MessageTest.java` and `MainTest.java` and cover:

- Message length: success and failure cases
- Recipient number validation: success and failure cases
- Message hash generation (including a loop over multiple messages)
- Message ID creation and the too-long ID case
- Send, disregard and store actions (storing writes the JSON file)
- Total message count and printing of sent messages
- End-to-end console flows, including a failed login blocking messaging

Run them with `mvn clean test`.

---

## Continuous integration

`maven.yml` defines a **Java CI with Maven** workflow. On every push or pull request to `main` it checks out the code, sets up Temurin JDK 25 (with Maven caching) and runs `mvn -B clean test`. Place it at `.github/workflows/maven.yml`.

---

## Known limitations

- "Show recently sent messages" is a placeholder and prints "Coming Soon."
- Sent messages are held in memory only and are lost when the program exits; only stored messages are written to disk.
- `messages.json` is rewritten with the stored messages from the current run, so it does not append to data from earlier runs.

---

## References

- Oracle, 2025. *Java Platform, Standard Edition Documentation*. Available at: https://docs.oracle.com/en/java/javase/
- JSON.org, n.d. *Introducing JSON*. Available at: https://www.json.org/
├── pom.xml
└── README.md
