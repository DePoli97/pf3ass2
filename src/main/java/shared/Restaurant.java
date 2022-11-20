package shared;

public interface Restaurant {
    /**
     * Receives an order from a client and queues it for cooking.
     * @param order The order to be queued.
     */
    void receive(Order order) throws InterruptedException;

    /**
     * Retrieves an order from the queue to be cooked.
     * @return The order to cook.
     */
    Order cook() throws InterruptedException;
}
