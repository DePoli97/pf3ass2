package ex2;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * An implementation of {@code Restaurant} which uses semaphores to ensure thread-safety and avoid spinning.
 * Can accept an unbounded number of orders.
 */
public class UnboundedSemaphoreRestaurant implements Restaurant {
    private final Queue<Order> queue = new LinkedList<>();
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore empty = new Semaphore(0);

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) {
        // Acquire the mutex to ensure mutual exclusion.
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Add the order to the queue.
        queue.add(order);
        // Release the mutex.
        mutex.release();
        // Signal that the queue is no longer empty.
        empty.release();
    }

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    @Override
    public Order cook() throws InterruptedException {
        // Wait until the queue is no longer empty.
        empty.acquire();
        // Acquire the mutex to ensure mutual exclusion.
        mutex.acquire();
        // Remove the order from the queue.
        Order order = queue.remove();
        // Release the mutex.
        mutex.release();
        return order;
    }
}
