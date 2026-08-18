import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class LibraryGUI {
    private Library library;
    private JTextArea displayArea;
    private String currentUserRole;
    private String currentUsername;

    public LibraryGUI(String role, String username) {
        this.currentUserRole = role;
        this.currentUsername = username;
        library = new Library();

        JFrame frame = new JFrame("Library System - Logged in as: " + role);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 700); // Made slightly taller to fit admin controls
        frame.setLayout(new BorderLayout(10, 10));

        // --- 1. Header Panel with Logo ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(230, 240, 250)); 
        
        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(" Welcome to the Central Library", new ImageIcon(scaledImage), JLabel.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(new Color(20, 50, 100)); 
        headerPanel.add(logoLabel);

        // --- 2. Top Control Panel ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Borrow Panel
        JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        borrowPanel.add(new JLabel("ISBN to Borrow:"));
        JTextField borrowIsbn = new JTextField(12);
        borrowPanel.add(borrowIsbn);
        
        borrowPanel.add(new JLabel("Member ID:"));
        JTextField borrowMemberId = new JTextField(12);
        if (currentUserRole.equals("Member")) {
            borrowMemberId.setText(currentUsername);
            borrowMemberId.setEditable(false);
            borrowMemberId.setBackground(Color.LIGHT_GRAY);
        }
        borrowPanel.add(borrowMemberId);
        
        JButton btnBorrow = new JButton("Borrow Book");
        borrowPanel.add(btnBorrow);
        topPanel.add(borrowPanel);

        // Return Panel
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        returnPanel.add(new JLabel("ISBN to Return:"));
        JTextField returnIsbn = new JTextField(12);
        returnPanel.add(returnIsbn);
        JButton btnReturn = new JButton("Return Book");
        returnPanel.add(btnReturn);
        
        if (currentUserRole.equals("Librarian")) {
            topPanel.add(returnPanel); 
        }

        // View Panel
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JButton btnViewBooks = new JButton("Show Available Books");
        viewPanel.add(btnViewBooks);

        JButton btnViewMembers = new JButton("Show All Members");
        JButton btnMyAccount = new JButton("My Account");

        if (currentUserRole.equals("Librarian")) {
            viewPanel.add(btnViewMembers); 
        } else if (currentUserRole.equals("Member")) {
            viewPanel.add(btnMyAccount); 
        }
        topPanel.add(viewPanel);

        // --- ADMIN CONTROLS (NEW) ---
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        adminPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 50, 50)), "Admin Controls", 0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(200, 50, 50)));
        JButton btnAddBook = new JButton("Add New Book");
        JButton btnAddMember = new JButton("Register New Member");
        
        adminPanel.add(btnAddBook);
        adminPanel.add(btnAddMember);
        
        if (currentUserRole.equals("Librarian")) {
            topPanel.add(adminPanel); // Only attach this panel if they are a librarian!
        }

        // --- 3. Text Area ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 15)); 
        displayArea.setBackground(new Color(250, 250, 250));
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "System Output"));

        // --- 4. Live Clock Status Bar ---
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(new Color(230, 230, 230));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        timeLabel.setForeground(Color.DARK_GRAY);
        statusPanel.add(timeLabel);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy  |  hh:mm:ss a");
        Timer timer = new Timer(1000, e -> timeLabel.setText(LocalDateTime.now().format(formatter)));
        timer.start();
        timeLabel.setText(LocalDateTime.now().format(formatter));

        // --- 5. Button Actions ---
        btnBorrow.addActionListener(e -> {
            String isbn = borrowIsbn.getText().trim();
            String memId = borrowMemberId.getText().trim();
            if(!isbn.isEmpty() && !memId.isEmpty()) {
                library.borrowBook(isbn, memId); 
                displayArea.setText("✅ Attempted to borrow book: " + isbn + "\nCheck terminal for status.");
                borrowIsbn.setText("");
                if(currentUserRole.equals("Librarian")) borrowMemberId.setText("");
            }
        });

        btnReturn.addActionListener(e -> {
            String isbn = returnIsbn.getText().trim();
            if(!isbn.isEmpty()) {
                library.returnBook(isbn); 
                displayArea.setText("🔄 Attempted to return book: " + isbn + "\nCheck terminal for status.");
                returnIsbn.setText("");
            }
        });

        btnViewBooks.addActionListener(e -> {
            displayArea.setText("--- Available Books ---\n\n");
            List<Book> allBooks = library.getBooks();
            boolean found = false;
            for (Book b : allBooks) {
                if (b.isAvailable()) {
                    displayArea.append("📖 ISBN: " + b.getIsbn() + "   |   Title: " + b.getTitle() + "\n");
                    found = true;
                }
            }
            if (!found) displayArea.append("No books currently available.\n");
        });

        btnViewMembers.addActionListener(e -> {
            displayArea.setText("--- Registered Members ---\n\n");
            List<Member> allMembers = library.getMembers();
            for (Member m : allMembers) {
                displayArea.append("👤 Member ID: " + m.getMemberId() + "   |   Name: " + m.getName() + "\n");
            }
        });

        btnMyAccount.addActionListener(e -> {
            displayArea.setText("--- My Account Details ---\n\n");
            displayArea.append("🔑 Logged in ID: " + currentUsername + "\n");
            displayArea.append("🛡️ Role: Member\n");
            for(Member m : library.getMembers()) {
                if(m.getMemberId().equals(currentUsername)) {
                    displayArea.append("👤 Name: " + m.getName() + "\n");
                }
            }
        });

        // --- NEW ADMIN ACTIONS ---
        btnAddBook.addActionListener(e -> {
            JTextField isbnField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            
            Object[] message = {
                "Book ISBN:", isbnField,
                "Book Title:", titleField,
                "Book Author:", authorField
            };
            
            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String newIsbn = isbnField.getText().trim();
                String newTitle = titleField.getText().trim();
                String newAuthor = authorField.getText().trim();
                
                if(!newIsbn.isEmpty() && !newTitle.isEmpty() && !newAuthor.isEmpty()) {
                    // CALL YOUR EXISTING BACKEND METHOD HERE
                    // Example: library.addBook(new Book(newIsbn, newTitle, newAuthor));
                    Book newBookObj = new Book(newIsbn, newTitle, newAuthor);
                    library.addBook(newBookObj);
                    displayArea.setText("📚 Registration Form Submitted for Book:\n");
                    displayArea.append("ISBN: " + newIsbn + " | Title: " + newTitle + "\n");
                    //displayArea.append("\n(Note: Ensure your Library.java method is called to save this to SQL!)");
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required to add a book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAddMember.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            
            Object[] message = {
                "New Member ID (e.g., M06):", idField,
                "Full Name:", nameField
            };
            
            int option = JOptionPane.showConfirmDialog(frame, message, "Register New Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String newId = idField.getText().trim();
                String newName = nameField.getText().trim();
                
                if(!newId.isEmpty() && !newName.isEmpty()) {
                    // CALL YOUR EXISTING BACKEND METHOD HERE
                    // Example: library.registerMember(new Member(newId, newName));
                    Member newMemberObj = new Member(newId, newName);
                    library.registerMember(newMemberObj);
                    displayArea.setText("👤 Registration Form Submitted for Member:\n");
                    displayArea.append("ID: " + newId + " | Name: " + newName + "\n");
                    //displayArea.append("\n(Note: Ensure your Library.java method is called to save this to SQL!)");
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required to register a member.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- 6. Assemble Window ---
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);
        wrapperPanel.add(topPanel, BorderLayout.CENTER);

        frame.add(wrapperPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH); 
        
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}