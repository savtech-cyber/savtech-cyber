# EChatApp

## Description

EChatApp is a simple Java-based chat application built with Swing and NetBeans JPanel forms. It provides user registration, login, and a basic chat interface. User credentials are stored in a Microsoft Access database (UserAndRegisterData.accdb) via the UCanAccess JDBC driver.

## Features

- **User Registration**: Collects first name, surname, phone number, username, and password.
- **Input Validation**:
  - Username availability check
  - Password complexity enforcement
  - South African phone number format validation
- **User Login**: Validates credentials against the Access database.
- **Chat Interface**: Displays a rudimentary chat window for sending and receiving messages (placeholder functionality).
- **Automated Tests**: JUnit tests for phone number validation and registration logic.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- NetBeans IDE (or any Java IDE)
- UCanAccess JDBC driver (includes the following JARs in the `lib/` directory):
  - `ucanaccess-x.x.x.jar`
  - `jackcess-x.x.x.jar`
  - `commons-lang-x.x.jar`
  - `commons-logging-x.x.jar`
  - `hsqldb.jar`
- Microsoft Access Database file (`UserAndRegisterData.accdb`)

## Setup

1. Clone or unzip the project into your workspace.
2. Add the UCanAccess driver JARs to the project classpath:
   - In NetBeans, right-click the project > **Properties** > **Libraries** > **Add JAR/Folder**.
3. Ensure the `UserAndRegisterData.accdb` file is located in the project root or update the connection string in `Login.java` accordingly.

## Running the Application

1. Open the project in NetBeans.
2. Run the `EChatApp` class (contains the `main` method).
3. The application will launch a window with the Welcome page.
4. Navigate between Registration, Login, and Chat pages via the GUI buttons.

## Project Structure

```
EChatApp/             # Project root
├── nbproject/        # NetBeans project metadata
├── UserAndRegisterData.accdb  # Access database file
├── build.xml         # Ant build script
└── src/
    └── echatapp/     # Java package
        ├── EChatApp.java        # Main launcher
        ├── WelcomePage.java     # Welcome screen JPanel
        ├── RegistrationPage.java# Registration form JPanel
        ├── LoginPage.java       # Login form JPanel
        ├── Login.java           # Registration/login validation and DB logic
        └── InChatPage.java      # Chat interface JPanel

EChatApp/test/echatapp/
└── LoginTest.java    # JUnit tests for validation methods
```

## Database Schema

The Access database contains a table for user registration with fields:

- `FirstName` (Text)
- `Surname` (Text)
- `PhoneNumber` (Text)
- `Username` (Text, Primary Key)
- `Password` (Text)

## Running Tests

In NetBeans, right-click the `test` folder or the `LoginTest.java` file and choose **Test** to execute the JUnit tests.

---


