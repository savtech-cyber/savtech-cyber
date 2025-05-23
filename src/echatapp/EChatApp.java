
package echatapp;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.SwingUtilities;


public class EChatApp {

   
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        
      
      // Run GUI code on the Event Dispatch Thread (good practice)
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("E Chat App"); // window title
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Replace MyPanelForm with your actual JPanel form class
            frame.setContentPane(new WelcomePage());
            frame.pack(); // sizes frame to fit the preferred size of its components
            frame.setLocationRelativeTo(null); // center on screen
            frame.setVisible(true);
            
            
            
        });  
    }
    
}
