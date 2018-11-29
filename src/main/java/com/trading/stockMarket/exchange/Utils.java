package com.trading.stockMarket.exchange;

import static java.lang.Thread.sleep;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Static class to place Utility lambda or methods
 * 
 * TODO rewrite and move this to a separate project or refactor using a known
 * library
 * 
 * @author Sanjay Ghosh
 *
 */
public class Utils {
	
	protected static final Logger LOGGER = LogManager.getLogger();

	/**
	 * 
	 * Returns a Blocking Queue of the List of elements provided
	 * 
	 * @param T
	 * @return BlockingQueue<T>
	 */
	public static <T> BlockingQueue<T> blockingQueue(List<T> listOft) {
		if (listOft != null && !listOft.isEmpty()) {
			BlockingQueue<T> arrayBlockingQueue = new ArrayBlockingQueue<>(listOft.size());
			listOft.forEach(t -> {
				try {
					arrayBlockingQueue.put(t);
				} catch (InterruptedException e) {
					System.err.println(" Interrupted Exception while adding " + t);
				}
			});
			return arrayBlockingQueue;
		}
		return null;
	}

	/**
	 * 
	 * Sleep Safely for the time period of miliseconds
	 * 
	 * 
	 */
	public static void sleepSafely(long timeOut) {
		try {
			LOGGER.info(" Sleep Safely for " + timeOut + " sec: ");
			sleep(timeOut);
		} catch (Exception e) {
			System.err.println(" Thrown from Sleep Safely: " + e.getCause());
		}
	}

	/**
	 * 
	 * Sleep Safely for the time period of miliseconds
	 * 
	 * Same as calling sleepSafely(1000) with parameter
	 * 
	 */
	public static void sleepSafely() {
		try {
			System.out.println(" Sleep Safely for 1 sec: ");
			sleep(1000);
		} catch (Exception e) {
			System.err.println(" Thrown from Sleep Safely: " + e.getCause());
		}
	}

	/**
	 * 
	 * Join the Thread safely
	 * 
	 */
	public static void joinSafely(Thread t) {
		try {
			System.out.println(" Join Safely: ");
			t.join();
		} catch (Exception e) {
			System.err.println(" Thrown from Join Safely: " + e.getCause());
		}
	}

	/**
	 * 
	 * Shutdown the executor service and run the consumer for each of the blocked
	 * Tasks that you have shutdown and are yet to be stopped
	 * 
	 * @param service
	 * @param consumer
	 */
	public static void shutDownGracefully(ExecutorService service, Consumer<Runnable> consumer) {
		service.shutdown();// No new tasks will be accepted
		try {
			while (!service.awaitTermination(5, TimeUnit.SECONDS)) { // Blocks for 5 seconds or if all tasks have been
																		// shutdown
				service.shutdownNow().forEach(consumer);// stop all
														// executing
														// tasks
				// and consume the ones which are running to the console.
			}
		} catch (InterruptedException e) {
			System.err.println(" Exception  " + e.getCause() + " while waiting for shutdown ");
		}
	}

	/**
	 * 
	 * Shutdown the executor service, wait for the provided time units and run the consumer for each of 
	 * Tasks that you have shutdown and are yet to be stopped
	 * 
	 * 
	 * @param ExecutorService
	 *            service
	 * @param Consumer<Runnable>
	 *            consumer
	 * @param Long
	 *            chrono
	 * @param TimeUnit
	 *            timeUnit
	 */
	public static void shutDownGracefully(ExecutorService service, Consumer<Runnable> consumer, Long chrono,
			TimeUnit timeUnit) {
		service.shutdown();// No new tasks will be accepted
		try {
			while (!service.awaitTermination(chrono, timeUnit)) { // Blocks for 5 seconds or if all tasks have been
																	// shutdown
				service.shutdownNow().forEach(consumer);// stop all
														// executing
														// tasks
				// and consume the ones which are running to the console.
			}
		} catch (InterruptedException e) {
			System.err.println(" Exception  " + e.getCause() + " while waiting for shutdown ");
		}
	}
}