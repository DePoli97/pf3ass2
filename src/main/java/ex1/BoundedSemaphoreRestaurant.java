package ex1;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * An implementation of {@code Restaurant} which uses semaphores to ensure thread-safety and avoid spinning.
 */
public class BoundedSemaphoreRestaurant implements Restaurant {

    private final int size;
    private final Queue<Order> queue = new LinkedList<>();
    private Semaphore fullSlots;
    private Semaphore emptySlots;
    private Semaphore mutex;
    /**
     * Constructs a new NaiveRestaurant with the given maximum queue size.
     * @param size The maximum number of orders this restaurant can process at once.
     */
    public BoundedSemaphoreRestaurant(int size) {
        this.size = size;
        fullSlots = new Semaphore(0);
        emptySlots = new Semaphore(size);
        mutex = new Semaphore(1);
    }

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) throws InterruptedException {
        emptySlots.acquire();

        mutex.acquire();

        while (queue.size() >= size) {
            if (Thread.interrupted()) throw new InterruptedException();
        }
        queue.add(order);

        mutex.release();

        fullSlots.release();
    }

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    @Override
    public Order cook() throws InterruptedException {

        fullSlots.acquire();

        mutex.acquire();
        
        while (queue.size() == 0) {
            if (Thread.interrupted()) throw new InterruptedException();
        }

        Order res = queue.poll();

        mutex.release();

        emptySlots.release();
        
        return res;
    }
}
