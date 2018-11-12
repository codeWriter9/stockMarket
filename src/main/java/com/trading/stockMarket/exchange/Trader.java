package com.trading.stockMarket.exchange;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class Trader implements Runnable {

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
	 * 
	 * 
	 * @param barrier
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
	 * 
	 * 
	 * @return
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
		System.out.println(traderid + " loading ");
		synchronized (queue) {
			for (Order order : ((ArrayBlockingQueue<Order>) queue)) {
				System.out.println(traderid + " checking " + order.brokerId());
				if (traderid.equals(order.brokerId())) {
					this.order = order;
					System.out.println(traderid + " this.order " + this.order);
					queue.remove(order);
				}
			}
			return this.order;
		}
	}

	/**
	 * 
	 * 
	 */
	private void addOrderToMatcher() {
		System.out.println(traderid + " adding to Matcher ");
		matcher.addOrder(load());
	}

	/**
	 * 
	 * 
	 */
	private void process() {
		System.out.println(traderid + " processing ");
		currentRun++;
		addOrderToMatcher();
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void run() {
		System.out.println(traderid + " running ");
		while (!done()) {
			System.out.println(traderid + " locking ");
			lock.lock();
			process();
			try {
				barrier.await();
			} catch (InterruptedException ex) {
				System.err.println(" Interrupted Exception from Trader " + ex.getMessage());
				return;
			} catch (BrokenBarrierException ex) {
				System.err.println(" BrokenBarrier Exception from Trader " + ex.getMessage());
				return;
			} finally {
				lock.unlock();
				System.out.println(traderid + " unlocking ");
			}
		}
	}
}