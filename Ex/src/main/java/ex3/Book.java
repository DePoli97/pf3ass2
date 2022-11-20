package ex3;

public class Book {
    private final String title;
    private final int readingTime;

    public Book(String title, int length) {
        this.title = title;
        this.readingTime = length;
    }

    public String title() {
        return title;
    }

    public void read() throws InterruptedException {
        Thread.sleep(readingTime);
    }
}
