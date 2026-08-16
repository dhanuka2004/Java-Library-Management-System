public class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String isbn, String title, String author) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true; // Books are available by default when added
    }

    public String getIsbn() { 
        return isbn; 
    }
    
    public String getTitle() { 
        return title; 
    }

    public String getAuthor() { 
        return author; 
    }
    
    public boolean isAvailable() { 
        return isAvailable; 
    }
    
    public void setAvailable(boolean available) { 
        isAvailable = available; 
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " + 
               (isAvailable ? "Available" : "Borrowed");
    }
}