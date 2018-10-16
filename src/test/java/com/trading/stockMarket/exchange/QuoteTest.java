package com.trading.stockMarket.exchange;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

public class QuoteTest {

	/**
	 * 
	 * 
	 */
	public static void sleepSafely() {
		try {
			sleep(1000);
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * Checks whether quotes are equal to each other *
	 * 
	 */
	@Test
	public void checkQuoteEquality() {
		List<Quote> asks = QuoteBuilder.askOf("AAA", 1, 10.0, 10.0, 10.0);
		asks.forEach(new Consumer<Quote>() {
			@Override
			public void accept(Quote quote) {
				assert (asks.get(0).equals(quote));
			}
		});
	}

	/**
	 * Checks whether quotes are unequal to each other *
	 * 
	 */
	@Test
	public void checkQuoteInequality() {
		Quote q1 = new Ask("AAA", 1, 10.0);
		sleepSafely();
		Quote q2 = new Ask("AAA", 1, 10.0);
		sleepSafely();
		Quote q3 = new Ask("AAA", 1, 10.0);
		sleepSafely();
		assertFalse(q1.equals(q2));
		assertFalse(q2.equals(q3));
	}
	
	
	/**
	 * Checks whether quotes are unequal to each other *
	 * 
	 */
	@Test
	public void checkOneQuoteFromList() {
		List<Quote> asks = QuoteBuilder.askOf("AAA", 1, 10.0, 10.0, 10.0);
		Quote q1 = asks.get(0);
		sleepSafely();
		Quote q2 = new Ask("AAA", 1, 10.0);				
		assertTrue(asks.contains(q1));
		assertFalse(asks.contains(q2));
	}
}