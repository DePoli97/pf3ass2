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
        rLock.lock();
        try{
            return movies.size();
        } finally {
            rLock.unlock();
        }
        // return 0;
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
        rLock.lock();
        try{
            return movies.get(title);
        } finally {
            rLock.unlock();
        }
        // return null;
    }

    /**
     * Returns the list of all book titles in this library
     *
     * @return a list of the book titles in this library
     */
    @Override
    public Set<String> getTitles() {
        // TODO: implement this method
        rLock.lock();
        try{
            return movies.keySet();
        } finally {
            rLock.unlock();
        }
        // return null;
    }

    /**
     * Returns a random book from this library, or null if the library is empty
     *
     * @return a randomly selected book from this catalog
     */
    @Override
    public Book getRandomBook() {
        // TODO: implement this method
        
        //here the code should generate a random number between 0 and the size of the library
        //and then return the book at that index
        //if the library is empty, return null
        rLock.lock();
        try{
            if(movies.size() == 0){
                return null;
            }
            int index = generator.nextInt(movies.size());
            return movies.values().toArray(new Book[0])[index];
        } finally {
            rLock.unlock();
        }

    }

    /**
     * Adds a book to the catalog
     *
     * @param book the book to be added to the catalog
     */
    @Override
    public void addBook(Book book) {
        // TODO: implement this method
        
        //this method uses locks to ensure that only one thread can add a book at a time and the method is used to add a book to the library
        wLock.lock();
        try{
            movies.put(book.title(), book);
        } finally {
            wLock.unlock();
        }
        

    }
}
