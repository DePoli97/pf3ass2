package ex2;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * An implementation of {@code Restaurant} which uses {@code ReentrantLock} to ensure thread-safety and avoid spinning.
 * Can accept an unbounded number of orders.
 */
public class UnboundedReentrantLockRestaurant implements Restaurant {
    private final Queue<Order> queue = new LinkedList<>();

	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) {
        lock.lock();
        try{
            queue.add(order);
            notEmpty.signal();
        } finally {
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
        lock.lock();
        try{
            while (queue.size() == 0) {
                try{
                    notEmpty.await();
                } catch (InterruptedException e){

                }
            }
            order = queue.poll();
        } finally {
            lock.unlock();
        }
        return order;
    }
}
