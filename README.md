# QuickChat – Java Messaging Application

## Project Overview
QuickChat is a Java console-based messaging application developed in three parts. The project demonstrates user registration, login authentication, message management, file storage, and automated testing.

## Part 1 – User Registration and Login
- Implemented user registration and login functionality.
- Validated usernames, passwords, and South African cellphone numbers.
- Verified user credentials during login.

## Part 2 – Messaging Functionality
- Implemented the QuickChat messaging system.
- Allowed users to send, disregard, and store messages.
- Generated unique message IDs and message hashes.
- Introduced JSON message storage.

## Part 3 – Message Management and Testing
- Implemented arrays to manage sent, disregarded, and stored messages.
- Added functionality to search and retrieve messages.
- Added message reporting and management features.
- Extended JSON file handling.
- Added JUnit tests to verify application functionality.

## Technologies Used
- Java
- Apache NetBeans
- Maven
- JUnit
- Gson (JSON processing)
- Git and GitHub

## How to Run
1. Open the project in Apache NetBeans.
2. Ensure Maven dependencies are available.
3. Run `Main.java`.
4. Register and log in.
5. Follow the QuickChat menu to manage messages.

## Testing
The project includes Maven-based JUnit tests for the login, main application, message, and message array classes.

## Project Structure
- `Login.java` – Registration and login.
- `Main.java` – Application execution and menus.
- `Message.java` – Message creation and processing.
- `MessageArrays.java` – Message collections and management.
- `pom.xml` – Maven project configuration.
