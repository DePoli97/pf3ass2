package ex3;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LibraryImpl implements Library{

    private final Map<String, Book> movies;
    private final ReentrantReadWriteLock rwlock;
    private final Lock rLock, wLock;
    private static final Random generator = new Random();

    public LibraryImpl() {
        movies = new HashMap<>();
        rwlock = new ReentrantReadWriteLock();
        rLock = rwlock.readLock();
        wLock = rwlock.writeLock();
    }

    /**
     * Returns the current size of this library
     *
     * @return the size of this library
     */
    @Override
    public int getSize() {
        // TODO: implement this method
        return 0;
    }

    /**
     * Returns a book from this library
     *
     * @param title the title of book to be obtained
     *
     * @return the book with the title provided or
     *         null if the book is not available
     */
    @Override
    public Book getBook(String title) {
        // TODO: implement this method
        return null;
    }

    /**
     * Returns the list of all book titles in this library
     *
     * @return a list of the book titles in this library
     */
    @Override
    public Set<String> getTitles() {
        // TODO: implement this method
        return null;
    }

    /**
     * Returns a random book from this library, or null if the library is empty
     *
     * @return a randomly selected book from this catalog
     */
    @Override
    public Book getRandomBook() {
        // TODO: implement this method
        return null;
    }

    /**
     * Adds a book to the catalog
     *
     * @param book the book to be added to the catalog
     */
    @Override
    public void addBook(Book book) {
        // TODO: implement this method

    }
}
