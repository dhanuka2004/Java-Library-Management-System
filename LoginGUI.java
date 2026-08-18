import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class LoginGUI {

    public LoginGUI() {
        JFrame loginFrame = new JFrame("Library System - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 450); // Made window taller for the image
        loginFrame.setLayout(new BorderLayout(10, 10)); // Added spacing

        // --- 1. Image Header Panel ---
        // Loads 'logo.png' from your folder and resizes it to 150x150 pixels
        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Padding

        // --- 2. Login Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        String[] roles = {"Librarian", "Member"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JTextField userText = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPasswordField passText = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180)); // Nice blue color
        loginButton.setForeground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        //JButton btnRegister = new JButton("Register New Member");


        formPanel.add(roleLabel);
        formPanel.add(roleComboBox);
        formPanel.add(userLabel);
        formPanel.add(userText);
        formPanel.add(passLabel);
        formPanel.add(passText);
        formPanel.add(loginButton);
        formPanel.add(cancelButton);
        //formPanel.add(btnRegister);


        // --- 3. Live Clock Panel (NEW) ---
        JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        timeLabel.setForeground(Color.DARK_GRAY);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // Format and Timer logic
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy  |  hh:mm:ss a");
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(formatter));
        });
        timer.start();
        timeLabel.setText(LocalDateTime.now().format(formatter)); // Set initial time immediately

        // --- 4. Add to Frame ---
        loginFrame.add(imageLabel, BorderLayout.NORTH);
        loginFrame.add(formPanel, BorderLayout.CENTER);
        loginFrame.add(timeLabel, BorderLayout.SOUTH);

        // Login Button Logic
        loginButton.addActionListener(e -> {
            String role = (String) roleComboBox.getSelectedItem();
            String username = userText.getText().trim();
            String password = new String(passText.getPassword());

            boolean loginSuccess = false;

            if (role.equals("Librarian") && username.equals("admin") && password.equals("admin123")) {
                loginSuccess = true;
            } else if (role.equals("Member") && username.startsWith("M") && password.equals("member123")) {
                loginSuccess = true;
            }

            if (loginSuccess) {
                loginFrame.dispose(); 
                SwingUtilities.invokeLater(() -> new LibraryGUI(role, username)); 
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

       /*  btnRegister.addActionListener(e -> {
    // 1. Create the text fields for the pop-up form
    JTextField newIdField = new JTextField();
    JTextField newNameField = new JTextField();
    JPasswordField newPasswordField = new JPasswordField();
    
    // 2. Build the visual form
    Object[] registrationForm = {
        "Full Name:", newNameField,
        "Desired Member ID (Username):", newIdField,
        "Password:", newPasswordField
    };
    
    // 3. Show the pop-up to the user
    int option = JOptionPane.showConfirmDialog(null, registrationForm, "Sign Up for Library", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
    // 4. If they clicked OK, process the data
    if (option == JOptionPane.OK_OPTION) {
        String newName = newNameField.getText().trim();
        String newId = newIdField.getText().trim();
        String newPass = new String(newPasswordField.getPassword()).trim();
        
        if (newName.isEmpty() || newId.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Instantiate your database class to save the user
            LibraryDatabase db = new LibraryDatabase();
            boolean success = db.registerNewMemberWithPassword(newId, newName, newPass);
            
            if (success) {
                JOptionPane.showMessageDialog(null, "Registration Successful! Welcome, " + newName + ".\nYou can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Registration Failed. That Member ID might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    });*/

        cancelButton.addActionListener(e -> System.exit(0));

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // --- TURN ON MODERN NIMBUS THEME ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to default if Nimbus fails
        }

        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}