package com.trading.stockMarket.exchange;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This is a runnable which implements the Trader The job of this class is to
 * extract orders from the queue which are meant for this brokerid and set them
 * to the matcher. Once the cyclic barrier runs the matcher, the Trader can
 * extract the information or can start the next iteration
 * 
 * @author Sanjay Ghosh
 *
 */
public class Trader implements Runnable {

	protected static final Logger LOGGER = LogManager.getLogger();

	private Order order;
	private Matcher matcher;
	private BlockingQueue<Order> queue;
	private CyclicBarrier barrier;
	private Lock lock;
	private Integer numberOfRuns;
	private Integer currentRun;
	private String traderid;

	/**
	 * 
	 * Constructor
	 * 
	 * @param traderid
	 *            String Must be Unique
	 * @param barrier
	 *            CyclicBarrier On which this runnable Trader will wait
	 * @param numberOfRuns
	 *            Integer which decides the number of iteration
	 * @param queue
	 *            BlockingQueue<Order> from where this Trader can extract the next
	 *            Order
	 * @param matcher
	 *            Matcher which matches the Trade and is the barrier runner of the
	 *            Cyclic Barrier
	 */
	public Trader(String traderid, CyclicBarrier barrier, Integer numberOfRuns, BlockingQueue<Order> queue,
			Matcher matcher) {
		this.lock = new ReentrantLock();
		this.traderid = traderid;
		this.barrier = barrier;
		this.numberOfRuns = numberOfRuns;
		this.currentRun = 0;
		this.matcher = matcher;
		this.queue = queue;
	}

	/**
	 * 
	 * Control method of the run method which will decide if there is a next
	 * iteration
	 * 
	 * @return True if no more runs remain otherwise False
	 */
	private boolean done() {
		return numberOfRuns == currentRun;
	}

	/**
	 * 
	 * Takes the Order out of the queue
	 * 
	 * @param order
	 */
	private Order load() {
		// System.out.println(traderid + " loading ");
		synchronized (queue) {
			for (Order order : ((ArrayBlockingQueue<Order>) queue)) {
				// System.out.println(traderid + " checking " + order.brokerId());
				if (traderid.equals(order.brokerId())) {
					this.order = order;
					// System.out.println(traderid + " this.order " + this.order);
					queue.remove(order);
				}
			}
			return this.order;
		}
	}

	/**
	 * 
	 * Loads the Order from the queue and Adds the current order to the matcher
	 * 
	 */
	private void addOrderToMatcher() {
		// System.out.println(traderid + " adding to Matcher ");
		matcher.addOrder(load());
	}

	/**
	 * 
	 * process the run
	 * 
	 */
	private void process() {
		// System.out.println(traderid + " processing ");
		currentRun++;
		addOrderToMatcher();
	}

	/**
	 * 
	 * while there are still runs this method will extract the orders from the Queue
	 * and set them to the matcher Finally it would wait for the barrier. Once
	 * Barrier is done and there is no exception it can go for the next iteration
	 * 
	 */
	@Override
	public void run() {
		// System.out.println(traderid + " running ");
		while (!done()) {
			// System.out.println(traderid + " locking ");
			lock.lock();
			process();
			try {
				barrier.await();
			} catch (InterruptedException ex) {
				LOGGER.error(" Interrupted Exception from Trader ", ex);
				return;
			} catch (BrokenBarrierException ex) {
				LOGGER.error(" BrokenBarrier Exception from Trader ", ex);
				return;
			} finally {
				lock.unlock();
				LOGGER.info(traderid + " unlocking ");
			}
		}
	}
}