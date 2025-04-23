import java.util.*;

// =============================
// Exception Classes
// =============================
class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

// =============================
// Book Class
// =============================
class Book {
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;

    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void checkout() throws BookNotAvailableException {
        if (!isAvailable) {
            throw new BookNotAvailableException("This book is already checked out.");
        }
        isAvailable = false;
    }

    public void returnBook() {
        isAvailable = true;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title + " by " + author + " (" + genre + ")" + (isAvailable ? " - Available" : " - Checked Out");
    }
}

// =============================
// LibraryCollection Class
// =============================
class LibraryCollection implements Iterable<Book> {
    private Map<String, List<Book>> genreMap;

    public LibraryCollection() {
        genreMap = new HashMap<>();
    }

    public void addBook(Book book) {
        genreMap.putIfAbsent(book.getGenre(), new ArrayList<>());
        genreMap.get(book.getGenre()).add(book);
    }

    public Iterator<Book> getGenreIterator(String genre) {
        List<Book> books = genreMap.getOrDefault(genre, new ArrayList<>());
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks.iterator();
    }

    public Book findBookByTitle(String genre, String title) throws BookNotFoundException {
        List<Book> books = genreMap.getOrDefault(genre, new ArrayList<>());
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        throw new BookNotFoundException("Book not found in this genre.");
    }

    @Override
    public Iterator<Book> iterator() {
        List<Book> allAvailable = new ArrayList<>();
        for (List<Book> list : genreMap.values()) {
            for (Book book : list) {
                if (book.isAvailable()) {
                    allAvailable.add(book);
                }
            }
        }
        return allAvailable.iterator();
    }
}

// =============================
// Main Program
// =============================
public class LibraryTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryCollection library = new LibraryCollection();

        // Sample books
        library.addBook(new Book("1984", "George Orwell", "Dystopian"));
        library.addBook(new Book("Brave New World", "Aldous Huxley", "Dystopian"));
        library.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy"));
        library.addBook(new Book("Harry Potter", "J.K. Rowling", "Fantasy"));

        System.out.print("Enter genre to browse: ");
        String genre = scanner.nextLine();

        Iterator<Book> genreIterator = library.getGenreIterator(genre);
        System.out.println("Available books in " + genre + ":");
        while (genreIterator.hasNext()) {
            System.out.println("- " + genreIterator.next());
        }

        try {
            System.out.print("Enter the title of the book to checkout: ");
            String titleToCheckout = scanner.nextLine();
            Book bookToCheckout = library.findBookByTitle(genre, titleToCheckout);
            bookToCheckout.checkout();
            System.out.println("You checked out: " + bookToCheckout);
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.print("Enter the title of the book to return: ");
            String titleToReturn = scanner.nextLine();
            Book bookToReturn = library.findBookByTitle(genre, titleToReturn);
            bookToReturn.returnBook();
            System.out.println("You returned: " + bookToReturn);
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
