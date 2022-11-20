package shared;

import ex2.*;
import ex1.*;

import java.util.*;

public final class Main {
	
	public static final int CLIENTS = 100;
	public static final int CHEFS = 100;
	public static final int SIZE = 10;

	public static void main(String[] args) throws InterruptedException {
		runRestaurant(new NaiveRestaurant(SIZE), "NaiveRestaurant");
		runRestaurant(new BoundedSemaphoreRestaurant(SIZE), "BoundedSemaphoreRestaurant");
		// runRestaurant(new BoundedWaitNotifyRestaurant(SIZE), "BoundedWaitNotifyRestaurant");
		// runRestaurant(new BoundedReentrantLockRestaurant(SIZE), "BoundedReentrantLockRestaurant");
		// runRestaurant(new UnboundedSemaphoreRestaurant(), "UnboundedSemaphoreRestaurant");
		// runRestaurant(new UnboundedWaitNotifyRestaurant(), "UnboundedWaitNotifyRestaurant");
		// runRestaurant(new UnboundedReentrantLockRestaurant(), "UnboundedReentrantLockRestaurant");
	}

	private static void runRestaurant(Restaurant restaurant, String name) throws InterruptedException {
		final OrderImpl[] orders = new OrderImpl[CLIENTS];
		for (int i = 0; i < CLIENTS; i++) {
			orders[i] = new OrderImpl();
		}

		final List<Thread> clientsAndChefs = new ArrayList<>(CLIENTS + CHEFS);
		for (int i = 0; i < CLIENTS; i++) {
			final int index = i;
			clientsAndChefs.add(new Thread(() -> {
				try {
					restaurant.receive(orders[index]);
				} catch (InterruptedException ignored) {}
			}));
		}
		for (int i = 0; i < CHEFS; i++) {
			clientsAndChefs.add(new Thread(() -> {
				try {
					Order order = restaurant.cook();
					if (order != null) order.process();
				} catch (InterruptedException ignored) {}
			}));
		}
		Collections.shuffle(clientsAndChefs);

		for (Thread t : clientsAndChefs) t.start();
		for (Thread t : clientsAndChefs) {
			t.join(10000);
			if (t.isAlive()) {
				System.out.println(name + " failed: no activity for 10 seconds; liveness hazard suspected. Remaining threads interrupted.");
				for (Thread t2 : clientsAndChefs) t2.interrupt();
				return;
			}
		}

		boolean failed = false;
		int duplicates = 0;
		int losses = 0;
		for (OrderImpl order : orders) {
			if (order.isDuplicated()) {
				failed = true;
				duplicates++;
//				System.out.println(name + " failed: order #" + order.id + " has been cooked multiple times.");
			}
			if (order.isLost()) {
				failed = true;
				losses++;
//				System.out.println(name + " failed: order #" + order.id + " has not been fulfilled.");
			}
		}
		if (failed) {
			String err = name + " failed: ";
			if (duplicates > 0) err += duplicates + " orders have been cooked multiple times";
			if (duplicates > 0 && losses > 0) err += " and ";
			if (losses > 0) err += losses + " orders have not been fulfilled";
			err += ".";
			System.out.println(err);
		} else {
			System.out.println(name + " successfully processed all orders.");
		}
	}
}
