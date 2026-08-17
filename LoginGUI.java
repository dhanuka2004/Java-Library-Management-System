import java.awt.*;
import javax.swing.*;

public class LoginGUI {

    public LoginGUI() {
        // Create the Login Window
        JFrame loginFrame = new JFrame("Library System - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(350, 200);
        loginFrame.setLayout(new GridLayout(4, 2, 10, 10));

        // Create the UI Components
        JLabel roleLabel = new JLabel(" Select Role:");
        String[] roles = {"Librarian", "Member"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JLabel userLabel = new JLabel(" Username:");
        JTextField userText = new JTextField();

        JLabel passLabel = new JLabel(" Password:");
        JPasswordField passText = new JPasswordField(); // Hides password characters

        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        // Add Components to the Window
        loginFrame.add(roleLabel);
        loginFrame.add(roleComboBox);
        loginFrame.add(userLabel);
        loginFrame.add(userText);
        loginFrame.add(passLabel);
        loginFrame.add(passText);
        loginFrame.add(loginButton);
        loginFrame.add(cancelButton);

        // Login Button Logic
        loginButton.addActionListener(e -> {
            String role = (String) roleComboBox.getSelectedItem();
            String username = userText.getText().trim();
            String password = new String(passText.getPassword());

            boolean loginSuccess = false;

            // Check Librarian Credentials
            if (role.equals("Librarian")) {
                if (username.equals("admin") && password.equals("admin123")) {
                    loginSuccess = true;
                }
            } 
            // Check Member Credentials
            else if (role.equals("Member")) {
                // For now, any username starting with "M" and this universal password works
                if (username.startsWith("M") && password.equals("member123")) {
                    loginSuccess = true;
                }
            }

            // Grant or Deny Access
            if (loginSuccess) {
                JOptionPane.showMessageDialog(loginFrame, "Login Successful! Welcome, " + username);
                loginFrame.dispose(); // Close the login window

                
                // Open your main library system
                SwingUtilities.invokeLater(() -> new LibraryGUI(role, username)); 
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button Logic
        cancelButton.addActionListener(e -> System.exit(0));

        // Center the window on the screen and show it
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Start the application from the Login Screen
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}