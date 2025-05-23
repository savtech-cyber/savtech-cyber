package echatapp;

import java.sql.*;
import javax.swing.JOptionPane;

public class Login {
    private String FirstName;
    private String Surname;
    private String Number;
    private String Username;
    private String Password;
    //private String username;
    //private String password;
    
    //Constructor used during registration
    public Login(String FirstName, String Surname, String Number, String Username, String Password) {
        this.FirstName = FirstName;
        this.Surname = Surname;
        this.Number = Number;
        this.Username = Username;
        this.Password = Password;
    }
    //Constructor used during login
     public Login(String username, String password) {
         this.Username= username;
         this.Password= password;
    }

    //Validates that username contains underscore and is no more then 5 Characters
    public boolean checkUserName() {
        if (Username != null && Username.contains("_") && Username.length() <= 5) {
            JOptionPane.showMessageDialog(null, "Username successfully captured");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Username is not correctly formatted. Please ensure that your username contains an underscore and is no more than five characters in length.");
            return false;
        }
    }
    
    //Checks Password complexity Requirements
    public boolean checkPasswordComplexity() {
        if (Password.length() < 8) return showInvalidPassword();
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char c : Password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true; // (Oracle, 2023)
            else if (Character.isDigit(c)) hasDigit = true;   // (Oracle, 2023)
            else if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;  // (Oracle, 2023)
        }
        if (hasUppercase && hasDigit && hasSpecialChar) {
            JOptionPane.showMessageDialog(null, "Password successfully captured.");
            return true;
        } else {
            return showInvalidPassword();
        }
    }
    //For INvalid password fORMATTING 
    private boolean showInvalidPassword() {
        JOptionPane.showMessageDialog(null, "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        return false;
    }
   
    //Validates international phone numbers that start with + and contain only digits
    public boolean checkPhoneNumber() {
        String phoneNumber= this.Number;
    // Check if it starts with '+' and contains only digits after
    if (!phoneNumber.startsWith("+")) {
        JOptionPane.showMessageDialog(null,"Cell phone number incorrectly formatted or does not contain international code.");
        return false;
    }

    String numberPart = phoneNumber.substring(1); // Remove '+' for counting digits

    if (!numberPart.matches("\\d{1,13}")) { // Up to 13 digits total (country code + number)
         JOptionPane.showMessageDialog(null,"Cell phone number incorrectly formatted or does not contain international code.");
        return false;
    }

    // Optional: split country code vs actual number
    // Assume country code is 2-3 digits
    if (numberPart.length() <= 3 || numberPart.length() > 13) {
        System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        return false;
    }

     JOptionPane.showMessageDialog(null,"Cell phone number successfully added.");
    return true;
    }

    
    
    // Register user method to microsoft accessDB using UCanAccess
    public static String registerUser(String Username, String Password, String Number, String FirstName, String Surname) {
        try {
            
            // Load the UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); //(UCanAccess,2023)

            // Database URL
            String databaseURL = "jdbc:ucanaccess://C:\\Users\\User\\Documents\\NetBeansProjects\\EChatApp\\UserAndRegisterData.accdb";
            Connection connection = DriverManager.getConnection(databaseURL); // (Microsoft, 2023)
            
            String query = "SELECT * FROM Register WHERE Username = ? OR Password = ? OR PhoneNumber = ? OR FirstName = ? OR Surname = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, Username);
            pst.setString(2, Password);
            pst.setString(3, Number);
            pst.setString(4, FirstName);
            pst.setString(5, Surname);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()){
               rs.close();
               pst.close();
               connection.close();
               return "One or more details Already exist";  
        } else{

            // Prepare the SQL INSERT statement
            String sql = "INSERT INTO Register (Username, Password,PhoneNumber, FirstName, Surname) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set the values from the text fields
            ps.setString(1, Username);
            ps.setString(2, Password);
            ps.setString(3, Number);
            ps.setString(4, FirstName);
            ps.setString(5, Surname);

            // Execute the update (insert data into the database)
            ps.executeUpdate();
            
            // Close the connection
            ps.close();
            connection.close();
            return "Registration successful !";
            }

            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Login verification method
    public static boolean loginUser(String username, String password) {
        try {
            
            // Load the UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");// (UCanAccess, 2023)

            // Database URL
            String databaseURL = "jdbc:ucanaccess://C:\\Users\\User\\Documents\\NetBeansProjects\\EChatApp\\UserAndRegisterData.accdb";
            Connection connection = DriverManager.getConnection(databaseURL); //(Microsoft, 2023)

            // Prepare the SQL SELECT statement to find matching username and password
            String sql = "SELECT * FROM Register WHERE Username = ? AND Password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            // User found, get first name and last name
            String FirstName = rs.getString("FirstName");
            String Surname = rs.getString("Surname");
            // Optional: Display the welcome message (outside the method)
            JOptionPane.showMessageDialog(null, "Welcome " + FirstName + ", " + Surname + ". It is great to see you.");
           // User found, successful login
                return true;
            } else {
                // User not found, failed login
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
  
   /* public static String returnLoginStatus(String Username, String Password) {
    boolean loginSuccess = loginUser(Username, Password);

    if (loginSuccess) {
        return "Login successful.";
    } else {
        return "Invalid Username or password incorrect.";
    }
    }*/
    /*
 * References:
 * Oracle (2023) Java Platform, Standard Edition 8 API Specification. 
 * Available at: https://docs.oracle.com/javase/8/docs/api/ 
 * [Accessed: 19 April 2025]
 *
 * Microsoft (2023) Create a database in Access. 
 * Available at: https://support.microsoft.com/en-us/office/create-a-database-in-access-4e926d26-8f7b-4cb4-bb8c-ae4f6d2b5b0d 
  [Accessed: 10 April 2025]
 *
 * UCanAccess (2023) UCanAccess JDBC Driver. 
 * Available at: https://ucanaccess.sourceforge.net/site.html 
  [Accessed: 20 April 2025]
 */
    
/***************************************************************************************
*    Title: Java Registration & Login
*    Author: ChatGPT (OpenAI)
*    Date: 20 April 2025
*    Code version: GPT-4
*    Availability: https://chat.openai.com/
*    Description: Assist in guideline code for  registration and login data validation
***************************************************************************************/

}



     