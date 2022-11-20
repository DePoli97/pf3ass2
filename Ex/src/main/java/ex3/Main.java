package ex3;

import java.util.*;

public class Main {

    private static final int INITIAL_SIZE = 500;
    private static final int READERS = 150;
    private static final int BROWSERS = 100;
    private static final int LIBRARIANS = 30;

    private static final int MAX_BOOK_LENGTH = 1000;
    private static final int MAX_PONDER_TIME = 10;

    public static void main(String[] args) throws InterruptedException {
        final Library library = new LibraryImpl();
        final Random rand = new Random();
        final List<String> addedBooks = new ArrayList<>();

        for (int i = 0; i < INITIAL_SIZE; i++) {
            addedBooks.add("Book #" + i);
            library.addBook(new Book("Book #" + i, rand.nextInt(MAX_BOOK_LENGTH)));
        }

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < READERS; i++) {
            final int id = i;
            threads.add(new Thread(() -> {
                try {
                    Book book = library.getRandomBook();
                    if (book == null) {
                        System.out.println("getRandomBook() returned null to reader #" + id
                                           + " even though the library is not empty");
                    } else {
                        book.read();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Reader #" + id + " was interrupted while reading");
                }
            }));
        }

        for (int i = 0; i < BROWSERS; i++) {
            final int id = i;
            threads.add(new Thread(() -> {
                try {
                    Set<String> books = library.getTitles();
                    if (books == null) {
                        System.out.println("getTitles() returned null to browser #" + id);
                    } else {
                        for (String book : books) {
                            Book returnedBook = library.getBook(book);
                            if (returnedBook == null) {
                                System.out.println("getBook() returned null to browser #" + id);
                            }
                            Thread.sleep(rand.nextInt(MAX_PONDER_TIME));
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Browser #" + id + " was interrupted while browsing");
                }
            }));
        }

        for (int i = 0; i < LIBRARIANS; i++) {
            final int id = i;
            addedBooks.add("Book #" + (INITIAL_SIZE + id));
            threads.add(new Thread(() -> {
                library.addBook(new Book("Book #" + (INITIAL_SIZE + id), rand.nextInt(MAX_BOOK_LENGTH)));
            }));
        }

        Collections.shuffle(threads);

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        for (String book : addedBooks) {
            if (library.getTitles() != null && !library.getTitles().contains(book)) {
                System.out.println(book + " is missing from the library despite having been added to it.");
            }
        }
        System.out.println("All threads terminated");
    }
}
