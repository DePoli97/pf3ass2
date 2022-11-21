package ex1;

import shared.Order;
import shared.Restaurant;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An implementation of {@code Restaurant} which uses {@code wait()} and {@code notify()} to ensure thread-safety and avoid spinning.
 */
public class BoundedWaitNotifyRestaurant implements Restaurant {
    private final int size;
    private final Queue<Order> queue = new LinkedList<>();

    /**
     * Constructs a new NaiveRestaurant with the given maximum queue size.
     * @param size The maximum number of orders this restaurant can process at once.
     */
    public BoundedWaitNotifyRestaurant(int size) {
        this.size = size;
    }

    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    @Override
    public void receive(Order order) throws InterruptedException {
        
        //Thread safe implementation using wait() and notify() to avoid errors
        processOrder();
        synchronized(this){
            queue.add(order);
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
        synchronized (this){
            while (queue.size() == 0) {
                try{
                    wait();
                } catch (InterruptedException e){

                }
            } 
            order = queue.poll();
            notifyAll();
        }
        processOrder();
        return order;
    }

    private static void processOrder() throws InterruptedException {
        Thread.sleep(100);
    }
}
