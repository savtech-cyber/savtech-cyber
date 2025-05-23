
package echatapp;
import echatapp.Login;
import java.awt.Component;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;




public class LoginTest {
    
    public LoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checkUserName method, of class Login.
     */
    @Test
    public void testCheckUserName() {
        System.out.println("Welcome John Doe its great to see you");
        // Create instance with valid username "kyl_1"
        Login instance = new Login("John", "Doe", "12345", "kyl_1", "password");
        // Test the checkUserName() method
        boolean expResult = true; // Expect true since the username is "kyl_1"
        boolean result = instance.checkUserName();
        // Print the result of the check
        System.out.println("Result of checkUserName: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
         
    }
    
     @Test
    public void testCheckUserNameInvalid() {
       System.out.println("checkUserName - Valid Username");
        // Create instance with valid username "kyl_1"
        Login instance = new Login("John", "Doe", "12345", "kyle!!!!!", "password");
        // Test the checkUserName() method
        boolean expResult = false; // Expect true since the username is "kyle!!!!!"
        boolean result = instance.checkUserName();
        // Print the result of the check
        System.out.println("Result of checkUserName: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
    }

    /**
     * Test of checkPasswordComplexity method, of class Login.
     */
    @Test
    public void testCheckPasswordComplexity() {
        System.out.println("checkPasswordComplexity");
        Login instance = new Login("John", "Doe", "12345", "kyle!!!!!", "Ch&&sec@ke99!");
        // Test the checkUserName() method
        boolean expResult = true; // Expect true since the username is "kyle!!!!!"
        boolean result = instance.checkPasswordComplexity();
        // Print the result of the check
        System.out.println("Result of checkPasswordComplexity: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
       
    }
    @Test
     public void testCheckPasswordComplexityInvalid() {
        System.out.println("checkPasswordComplexity");
        Login instance = new Login("John", "Doe", "12345", "kyle!!!!!", "password");
        // Test the checkUserName() method
        boolean expResult = false; // Expect true since the username is "kyle!!!!!"
        boolean result = instance.checkPasswordComplexity();
        // Print the result of the check
        System.out.println("Result of checkPasswordComplexity: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
       
    }

    /**
     * Test of checkPhoneNumber method, of class Login.
     */
    @Test
    public void testCheckPhoneNumber() {
        System.out.println("checkPhoneNumber");
        Login instance = new Login("John", "Doe", "+27838968976", "kyle!!!!!", "password");
        // Test the checkUserName() method
        boolean expResult = true; // Expect true since the username is "kyle!!!!!"
        boolean result = instance.checkPhoneNumber();
        // Print the result of the check
        System.out.println("Result of CheckPhoneNumber: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
       
        
    }
    
    @Test
    public void testCheckPhoneNumberInvalid() {
        System.out.println("checkPhoneNumber");
        Login instance = new Login("John", "Doe", "08966553", "kyle!!!!!", "password");
        // Test the checkUserName() method
        boolean expResult = false; // Expect true since the username is "kyle!!!!!"
        boolean result = instance.checkPhoneNumber();
        // Print the result of the check
        System.out.println("Result of CheckPhoneNumber: " + result);
        // Assert that the result matches the expected result
        assertEquals(expResult, result);
       
        
    }
    
    @Test
public void testLoginSuccess() {
    Login.registerUser("kyl_1", "CorrectPass1!", "+27831234567", "John", "Doe");
    // Assumes this user is registered with hashed password
    boolean result = Login.loginUser("kyl_1", "CorrectPass1!");
    System.out.println("Result of CheckLoginSuccess: " + result);
    assertTrue(result);  // Expected: login success
}

@Test
public void testLoginFailure() {
    Login.registerUser("kyl_1", "CorrectPass1!", "+27831234567", "John", "Doe");
    boolean result = Login.loginUser("kyl_1", "WrongPass");
    System.out.println("Result of CheckLoginFailure: " + result);
    assertFalse(result); // Expected: login failed
}

@Test
    public void testCheckUserName_Correct() {
        Login user = new Login("John", "Doe", "+27831234567", "kyl_1", "Passw0rd!");
        boolean result = user.checkUserName();
        System.out.println("Username correctly formatted: " + result); // True
        assertTrue(result);
    }

    @Test
    public void testCheckUserName_Incorrect() {
        Login user = new Login("John", "Doe", "+27831234567", "kylelongname", "Passw0rd!");
        boolean result = user.checkUserName();
        System.out.println("Username incorrectly formatted: " + result); // False
        assertFalse(result);
    }

    @Test
    public void testCheckPasswordComplexity_Correct() {
        Login user = new Login("John", "Doe", "+27831234567", "kyl_1", "Ch&&sec@ke99!");
        boolean result = user.checkPasswordComplexity();
        System.out.println("Password meets complexity requirements: " + result); // True
        assertTrue(result);
    }

    @Test
    public void testCheckPasswordComplexity_Incorrect() {
        Login user = new Login("John", "Doe", "+27831234567", "kyl_1", "simplepass");
        boolean result = user.checkPasswordComplexity();
        System.out.println("Password does not meet complexity requirements: " + result); // False
        assertFalse(result);
    }

    @Test
    public void testCheckPhoneNumber_Correct() {
        Login user = new Login("John", "Doe", "+27831234567", "kyl_1", "Passw0rd!");
        boolean result = user.checkPhoneNumber();
        System.out.println("Cell phone number correctly formatted: " + result); // True
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumber_Incorrect() {
        Login user = new Login("John", "Doe", "0831234567", "kyl_1", "Passw0rd!");
        boolean result = user.checkPhoneNumber();
        System.out.println("Cell phone number incorrectly formatted: " + result); // False
        assertFalse(result);
    }
}

