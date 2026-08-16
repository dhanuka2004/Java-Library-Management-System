public class LibraryManagementSystem {
    public static void main(String[] args) {
        
        LibraryDatabase database = new LibraryDatabase();
        database.createTable(); // <-- Ensures table exists in SSMS first

        Library library = new Library();

        System.out.println("Initializing Library System...\n");

        // Add Books
        /*  library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "1111"));
        library.addBook(new Book("1984", "George Orwell", "2222"));
        library.addBook(new Book("Clean Code", "Robert C. Martin", "3333"));
        library.addBook(new Book("dhanuka dilsara", "bro Orwell", "4444"));*/
        //library.addBook(new Book("pola santha", "stive smith", "6666"));



        // Register Members
        /*library.registerMember(new Member("Alice", "M01"));
        library.registerMember(new Member("Bokka", "M02"));
        library.registerMember(new Member("Bob", "M03"));
        library.registerMember(new Member("Bosa", "M04"));
        library.registerMember(new Member("amarasiri pieris", "M05"));*/

        // Display initially available books
        library.displayAvailableBooks();

        // Simulate Borrowing
        //library.borrowBook("3333", "M01"); // Alice borrows Gatsby
       // library.borrowBook("1111", "M02"); // Bob tries to borrow Gatsby (fails)

        // Display after borrowing
       // library.displayAvailableBooks();

        // Simulate Returning
        library.returnBook("2222"); // Gatsby is returned

        // Final inventory check
       // library.displayAvailableBooks();
    }
}