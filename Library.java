import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    // --- LINE 1: Create the connection to your database class ---
    private LibraryDatabase db = new LibraryDatabase();

    public Library() {
        this.books = db.getAllBooks();
        this.members = db.getAllMembers();
    }
    
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added Book: " + book.getTitle());
        db.addBook(book);
    }

    public void registerMember(Member member) {
        members.add(member);
        System.out.println("Registered Member: " + member.getName());
        db.addMember(member);
    }
    

    public void borrowBook(String isbn, String memberId) {
        Optional<Book> bookOpt = books.stream()
                                      .filter(b -> b.getIsbn().equals(isbn))
                                      .findFirst();
        Optional<Member> memberOpt = members.stream()
                                            .filter(m -> m.getMemberId().equals(memberId))
                                            .findFirst();

        if (bookOpt.isPresent() && memberOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.isAvailable()) {
                book.setAvailable(false);
                db.updateBookStatus(isbn, false);
                System.out.println("SUCCESS: " + memberOpt.get().getName() + " borrowed '" + book.getTitle() + "'");
            } else {
                System.out.println("UNAVAILABLE: '" + book.getTitle() + "' is currently borrowed out.");
            }
        } else {
            System.out.println("ERROR: Invalid Book ISBN or Member ID.");
        }
    }

    public void returnBook(String isbn) {
        Optional<Book> bookOpt = books.stream()
                                      .filter(b -> b.getIsbn().equals(isbn))
                                      .findFirst();
        
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (!book.isAvailable()) {
                book.setAvailable(true);
                db.updateBookStatus(isbn, true);
                System.out.println("SUCCESS: Returned '" + book.getTitle() + "'");
            } else {
                System.out.println("INFO: This book was not borrowed.");
            }
        } else {
            System.out.println("ERROR: Book not found in library.");
        }
    }

    public void displayAvailableBooks() {
        System.out.println("\n--- Available Books ---");
        boolean hasAvailable = false;
        for (Book b : books) {
            if (b.isAvailable()) {
                System.out.println(b);
                hasAvailable = true;
            }
        }
        if (!hasAvailable) {
            System.out.println("No books currently available.");
        }
        System.out.println("-----------------------\n");
    }

    // Helper method for the GUI to read the book list
    public List<Book> getBooks() {
        return this.books;
    }

    // Helper method for the GUI to read the member list
    public List<Member> getMembers() {
        return this.members;
    }
   

}