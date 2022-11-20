package ex3;

import java.util.Set;

public interface Library {
    /**
     * Returns the current size of this library
     *
     * @return the size of this library
     */
    int getSize();

    /**
     * Returns a book from this library
     *
     * @param title the title of book to be obtained
     *
     * @return the book with the title provided or
     *         null if the book is not available
     */
    Book getBook(String title);

    /**
     * Returns the list of all book titles in this library
     *
     * @return a list of the book titles in this library
     */
    Set<String> getTitles();

    /**
     * Returns a random book from this library, or null if the library is empty
     *
     * @return a randomly selected book from this library
     */
    Book getRandomBook();

    /**
     * Adds a book to the library
     *
     * @param book the book to be added to the library
     */
    void addBook(Book book);
}
