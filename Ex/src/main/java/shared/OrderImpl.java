package shared;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderImpl implements Order{
    private static final AtomicInteger nextId = new AtomicInteger(0);
    public final int id = nextId.getAndIncrement();
    private volatile boolean cooked = false;
    private volatile boolean duplicated = false;

    /**
     * Cooks the dish represented by this order.
     */
    @Override
    public synchronized void process() {
        if (!cooked) {
            cooked = true;
        } else {
            duplicated = true;
        }
    }

    /**
     * Whether this order has been lost. Only accurate if called while the queue is empty.
     * @return {@code true} iff this order has never been processed.
     */
    public boolean isLost() {
        return !cooked;
    }

    /**
     * Whether this order has been duplicated.
     * @return {@code true} iff this order has been processed multiple times.
     */
    public boolean isDuplicated() {
        return duplicated;
    }
}
