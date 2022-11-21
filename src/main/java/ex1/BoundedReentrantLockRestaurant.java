package ex1;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;


import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * An implementation of {@code Restaurant} which uses {@code ReentrantLock} to ensure thread-safety and avoid spinning.
 */
public class BoundedReentrantLockRestaurant implements Restaurant {
    private final int size;
    private final Queue<Order> queue = new LinkedList<>();
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();

    /**
     * Constructs a new NaiveRestaurant with the given maximum queue size.
     * @param size The maximum number of orders this restaurant can process at once.
     */
    public BoundedReentrantLockRestaurant(int size) {
        this.size = size;
    }

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) throws InterruptedException {
        // while (queue.size() >= size) {
        //     if (Thread.interrupted()) throw new InterruptedException();
        // }
        // queue.add(order);

        processOrder();
        lock.lock();
        try{
            notEmpty.signal();
        } finally {
            queue.add(order);
            lock.unlock();
        }
    }

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    @Override
    public Order cook() throws InterruptedException {
        Order order;
        processOrder();
        lock.lock();
        try{
            while (queue.size() == 0) {
                notEmpty.await();
            }
        } finally {
            order =  queue.poll();
            lock.unlock();
        }
        processOrder();
        return order;
    }


    private static void processOrder() throws InterruptedException {
        Thread.sleep(100);
    }
}
