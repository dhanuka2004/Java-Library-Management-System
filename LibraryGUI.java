import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LibraryGUI {
    private Library library;
    private JTextArea displayArea;
    
    // Store the logged-in user's details
    private String currentUserRole;
    private String currentUsername;

    // Constructor now requires role and username
    public LibraryGUI(String role, String username) {
        this.currentUserRole = role;
        this.currentUsername = username;
        library = new Library();

        // 1. Create the Main Window
        JFrame frame = new JFrame("Library System - Logged in as: " + role);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLayout(new BorderLayout());

        // 2. Create the Top Control Panel (BoxLayout lets us stack panels easily)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Borrow Panel (Visible to Both) ---
        JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        borrowPanel.add(new JLabel("ISBN to Borrow:"));
        JTextField borrowIsbn = new JTextField(10);
        borrowPanel.add(borrowIsbn);
        
        borrowPanel.add(new JLabel("Member ID:"));
        JTextField borrowMemberId = new JTextField(10);
        // If it's a member, lock the ID field to their own username!
        if (currentUserRole.equals("Member")) {
            borrowMemberId.setText(currentUsername);
            borrowMemberId.setEditable(false);
        }
        borrowPanel.add(borrowMemberId);
        
        JButton btnBorrow = new JButton("Borrow Book");
        borrowPanel.add(btnBorrow);
        topPanel.add(borrowPanel);

        // --- Return Panel (Librarian Only) ---
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        returnPanel.add(new JLabel("ISBN to Return:"));
        JTextField returnIsbn = new JTextField(10);
        returnPanel.add(returnIsbn);
        JButton btnReturn = new JButton("Return Book");
        returnPanel.add(btnReturn);
        
        if (currentUserRole.equals("Librarian")) {
            topPanel.add(returnPanel); // Only add this panel if they are a librarian
        }

        // --- View Panel (Dynamic based on role) ---
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnViewBooks = new JButton("Show Available Books");
        viewPanel.add(btnViewBooks);

        JButton btnViewMembers = new JButton("Show All Members");
        JButton btnMyAccount = new JButton("My Account");

        if (currentUserRole.equals("Librarian")) {
            viewPanel.add(btnViewMembers); // Librarians see all members
        } else if (currentUserRole.equals("Member")) {
            viewPanel.add(btnMyAccount); // Members see their own account
        }
        topPanel.add(viewPanel);

        // 3. Create the Text Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Output"));

        // 4. Add Button Click Actions
        btnBorrow.addActionListener(e -> {
            String isbn = borrowIsbn.getText().trim();
            String memId = borrowMemberId.getText().trim();
            if(!isbn.isEmpty() && !memId.isEmpty()) {
                library.borrowBook(isbn, memId); 
                displayArea.setText("Attempted to borrow book: " + isbn + "\nCheck terminal for status.");
                borrowIsbn.setText("");
                if(currentUserRole.equals("Librarian")) borrowMemberId.setText("");
            }
        });

        btnReturn.addActionListener(e -> {
            String isbn = returnIsbn.getText().trim();
            if(!isbn.isEmpty()) {
                library.returnBook(isbn); 
                displayArea.setText("Attempted to return book: " + isbn + "\nCheck terminal for status.");
                returnIsbn.setText("");
            }
        });

        btnViewBooks.addActionListener(e -> {
            displayArea.setText("--- Available Books ---\n");
            List<Book> allBooks = library.getBooks();
            boolean found = false;
            for (Book b : allBooks) {
                if (b.isAvailable()) {
                    displayArea.append("ISBN: " + b.getIsbn() + " | Title: " + b.getTitle() + "\n");
                    found = true;
                }
            }
            if (!found) displayArea.append("No books currently available.\n");
        });

        btnViewMembers.addActionListener(e -> {
            displayArea.setText("--- Registered Members ---\n");
            List<Member> allMembers = library.getMembers();
            for (Member m : allMembers) {
                displayArea.append("Member ID: " + m.getMemberId() + " | Name: " + m.getName() + "\n");
            }
        });

        // NEW: My Account Action (For Members)
        btnMyAccount.addActionListener(e -> {
            displayArea.setText("--- My Account Details ---\n");
            displayArea.append("Logged in ID: " + currentUsername + "\n");
            displayArea.append("Role: Member\n");
            
            // Search the member list to get their real name
            for(Member m : library.getMembers()) {
                if(m.getMemberId().equals(currentUsername)) {
                    displayArea.append("Name: " + m.getName() + "\n");
                }
            }
        });

        // 5. Assemble and Show
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}