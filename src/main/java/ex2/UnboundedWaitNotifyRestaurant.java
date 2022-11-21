package ex2;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An implementation of {@code Restaurant} which uses {@code wait()} and {@code notify()} to ensure thread-safety and avoid spinning.
 * Can accept an unbounded number of orders.
 */
public class UnboundedWaitNotifyRestaurant implements Restaurant {
    private final Queue<Order> queue = new LinkedList<>();

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) {
        synchronized (this){
            queue.add(order);
            notify();
        }

    }

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    @Override
    public Order cook() throws InterruptedException {
        Order order;
        synchronized (this){
            while (queue.size() == 0) {
                try{
                    wait();
                } catch (InterruptedException e){

                }
            }
            order = queue.poll();
            notify();
        }
        return order;
    }
}
