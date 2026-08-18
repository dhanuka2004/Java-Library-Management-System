import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
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

    public boolean borrowBook(String isbn, String memberId) {
        String cleanIsbn = isbn.trim();
        String cleanMemberId = memberId.trim();
        
        //this codes creats beacause of check the output incorrect.
        /*System.out.println("DEBUG: Searching for ISBN: [" + cleanIsbn + "] and Member: [" + cleanMemberId + "]");
        System.out.println("--- Books Currently in Java Memory ---");
            for (Book b : books) {
                System.out.println("Loaded ISBN: '" + b.getIsbn() + "'");
        }
        System.out.println("--- Members Currently in Java Memory ---");
            for (Member m : members) {
                System.out.println("Loaded Member ID: '" + m.getMemberId() + "'");
        }*/

        // Trimming database strings handles SQL CHAR padding issues
        Optional<Book> bookOpt = books.stream()
                .filter(b -> b.getIsbn().trim().equalsIgnoreCase(cleanIsbn))
                .findFirst();

        Optional<Member> memberOpt = members.stream()
                .filter(m -> m.getMemberId().trim().equalsIgnoreCase(cleanMemberId))
                .findFirst();

        if (bookOpt.isPresent() && memberOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.isAvailable()) {
                book.setAvailable(false);
                db.updateBookStatus(cleanIsbn, false);
                System.out.println("SUCCESS: " + memberOpt.get().getName() + " borrowed '" + book.getTitle() + "'");
                return true;
            } else {
                System.out.println("UNAVAILABLE: '" + book.getTitle() + "' is currently borrowed out.");
                return false;
            }
        } else {
            System.out.println("ERROR: Invalid Book ISBN or Member ID.");
            return false;
        }
    }

    public boolean returnBook(String isbn) {
        String cleanIsbn = isbn.trim();

        Optional<Book> bookOpt = books.stream()
                .filter(b -> b.getIsbn().trim().equalsIgnoreCase(cleanIsbn))
                .findFirst();
        
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (!book.isAvailable()) {
                book.setAvailable(true);
                db.updateBookStatus(cleanIsbn, true);
                System.out.println("SUCCESS: Returned '" + book.getTitle() + "'");
                return true;
            } else {
                System.out.println("INFO: This book was not borrowed.");
                return false;
            }
        } else {
            System.out.println("ERROR: Book not found in library.");
            return false;
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

    public List<Book> getBooks() {
        return this.books;
    }

    public List<Member> getMembers() {
        return this.members;
    }
}