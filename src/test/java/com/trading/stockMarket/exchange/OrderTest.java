package com.trading.stockMarket.exchange;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class OrderTest {

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
	 * Checks whether quotes are equal to each other
	 * 
	 */
	@Test
	public void checkOrderEquality() {
		List<Quote> asks = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		sleepSafely();
		Order order = new Order("Trader1");

		order.addOrder("AAA", asks.get(0));
		order.addOrder("AAA", new Ask("AAA", 1, 10.0));

		assertTrue((order.checkQuotes("AAA", (quote) -> quote.getPrice() == 10.0)));
		asks.forEach((quote) -> quote.setPrice(quote.getPrice() + 1.0));// change a quote in the ask list but not
																		// directly
		assertFalse((order.checkQuotes("AAA", (quote) -> quote.getPrice() == 11.0)));
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void checkOrders() {
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);
		sleepSafely();
		List<Quote> quotes = new ArrayList<>();
		quotes.addAll(asksAAA);
		quotes.addAll(asksBBB);
		quotes.addAll(asksCCC);
		Order order = new Order("Trader1");

		// add all orders
		for (Quote quote : quotes) {
			order.addOrder(quote.getSymbol(), quote);
		}

		for (String symbol : order.symbols()) {
			if ("AAA".equals(symbol))
				order.quotesForSymbol(symbol).forEach(quote -> assertTrue(quote.getPrice().equals(10.0)));
			else if ("BBB".equals(symbol))
				order.quotesForSymbol(symbol).forEach(quote -> assertTrue(quote.getPrice().equals(11.0)));
		}

	}

	@Test
	public void checkTwoOrders() {
		List<Order> orders = new ArrayList<Order>();
		Matcher matcher = new Matcher(orders);

		System.out.println(" running checkTwoOrders ");
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);
		
		System.out.println(" running checkTwoOrders: Adding Order 1 ");
		Order order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running checkTwoOrders: Adding Order 2 ");
		Order order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running checkTwoOrders: starting Matcher ");
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(matcher);
		service.shutdown();// No new tasks will be accepted
		try {
			while(!service.awaitTermination(5, TimeUnit.SECONDS)) { // Blocks for 5 seconds or if all tasks have been shutdown
				service.shutdownNow().forEach((runnable) -> System.out.println(" waiting for " + runnable));// stop all executing tasks
				//  and consume the ones which are running to the console.
			}
		} catch (InterruptedException e) {
			System.err.println(" Exception  " + e.getCause() + " while waiting shutdown " );
		}
		System.out.println(" ask Orders " + matcher.askOrder());
		System.out.println(" bid Orders " + matcher.bidOrder());		
	}
}