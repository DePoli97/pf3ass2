package ex1;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An implementation of {@code Restaurant} which uses semaphores to ensure thread-safety and avoid spinning.
 */
public class BoundedSemaphoreRestaurant implements Restaurant {

    private final int size;
    private final Queue<Order> queue = new LinkedList<>();

    /**
     * Constructs a new NaiveRestaurant with the given maximum queue size.
     * @param size The maximum number of orders this restaurant can process at once.
     */
    public BoundedSemaphoreRestaurant(int size) {
        this.size = size;
    }

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) throws InterruptedException {
        while (queue.size() >= size) {
            if (Thread.interrupted()) throw new InterruptedException();
        }
        queue.add(order);
    }

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    @Override
    public Order cook() throws InterruptedException {
        while (queue.size() == 0) {
            if (Thread.interrupted()) throw new InterruptedException();
        }
        return queue.poll();
    }
}
