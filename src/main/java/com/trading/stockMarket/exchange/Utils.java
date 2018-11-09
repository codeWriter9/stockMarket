package com.trading.stockMarket.exchange;

import static java.lang.Thread.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 
 * Static class to place Utility lambda or methods
 * 
 * TODO rewrite and move this to a separate project or refactor using a known library
 * 
 * @author Sanjay Ghosh
 *
 */
public class Utils {
	
	/**
	 * 
	 * 
	 */
	public static void sleepSafely(long timeOut) {
		try {
			System.out.println(" Sleep Safely for " + timeOut  + " sec: ");
			sleep(timeOut);
		} catch (Exception e) {
			System.err.println(" Thrown from Sleep Safely: ");
			System.err.println(" Thrown from Sleep Safely: " + e.getCause());
		}
	}
	
	/**
	 * 
	 * 
	 */
	public static void sleepSafely() {
		try {
			System.out.println(" Sleep Safely for 1 sec: ");
			sleep(1000);
		} catch (Exception e) {
			System.err.println(" Thrown from Sleep Safely: ");
			System.err.println(" Thrown from Sleep Safely: " + e.getCause());
		}
	}
	
	
	/**
	 * 
	 * 
	 */
	public static void joinSafely(Thread t) {
		try {
			System.out.println(" Join Safely: ");
			t.join();
		} catch (Exception e) {
			System.err.println(" Thrown from Join Safely: ");
			System.err.println(" Thrown from Join Safely: " + e.getCause());
		}
	}
	
	/**
	 * 
	 * 
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
}