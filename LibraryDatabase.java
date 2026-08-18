import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibraryDatabase {
    
    // Update these details to match your SQL Server configuration
    // trustServerCertificate=true is required for local testing without SSL setup
    private static final String DB_URL = "jdbc:sqlserver://localhost\\MSSQLSERVER:1433;databaseName=LLLibraryDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";               // Default SQL Server admin user
    private static final String PASS = "123456789";  // Your SQL Server password

    // 1. Establish a connection to SQL Server
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    // 2. Create the table in SQL Server
    public void createTable() {
        // SQL Server uses 'BIT' for boolean values
        String bookTableSql = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='bbbooks' and xtype='U') "
                   + "CREATE TABLE bbbooks ("
                   + " isbn VARCHAR(50) PRIMARY KEY,"
                   + " title VARCHAR(255) NOT NULL,"
                   + " author VARCHAR(255) NOT NULL,"
                   + " isAvailable BIT NOT NULL"
                   + ");";

        String memberTableSql = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='members' and xtype='U') "
                              + "CREATE TABLE members ("
                              + " memberId VARCHAR(50) PRIMARY KEY,"
                              + " name VARCHAR(255) NOT NULL"
                              + ");";
        
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(bookTableSql);
                System.out.println("Database table ready in SQL Server.");
            
                stmt.execute(memberTableSql);
                System.out.println("Member table ('members') ready in SQL Server.");
            
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. Save a book permanently
    public void addBook(Book book) {
        String sql = "INSERT INTO bbbooks(isbn, title, author, isAvailable) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn != null) {
                pstmt.setString(1, book.getIsbn());
                pstmt.setString(2, book.getTitle());
                pstmt.setString(3, book.getAuthor());
                pstmt.setBoolean(4, book.isAvailable());
                
                pstmt.executeUpdate();
                System.out.println("Permanently Saved to SQL Server: " + book.getTitle());
            }
            
        } catch (SQLException e) {
            System.out.println("Error saving book: " + e.getMessage());
        }
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO members(memberId, name) VALUES(?,?)";
        
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn != null) {
                pstmt.setString(1, member.getMemberId());
                pstmt.setString(2, member.getName());
                
                pstmt.executeUpdate();
                System.out.println("Permanently Saved Member to SQL Server: " + member.getName());
            }
            
        } catch (SQLException e) {
            System.out.println("Error saving member: " + e.getMessage());
        }
    }

    // 5. Update book availability status in database
    public void updateBookStatus(String isbn, boolean isAvailable) {
        String sql = "UPDATE bbbooks SET isAvailable = ? WHERE isbn = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn != null) {
                pstmt.setBoolean(1, isAvailable);
                pstmt.setString(2, isbn);
                pstmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.out.println("Error updating book status: " + e.getMessage());
        }
    }

    //code use when compare the database and give the data as output
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT isbn, title, author, isAvailable FROM bbbooks";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean isAvailable = rs.getBoolean("isAvailable");

                Book book = new Book(isbn, title, author);
                book.setAvailable(isAvailable);
                bookList.add(book);
            }
            
        } catch (SQLException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        
        return bookList;
    }

    // Fetch all members from SQL Server
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();
        String sql = "SELECT memberId, name FROM members";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String memberId = rs.getString("memberId");
                String name = rs.getString("name");
                memberList.add(new Member(memberId,name ));
            }
            
        } catch (SQLException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
        
        return memberList;
    }

}