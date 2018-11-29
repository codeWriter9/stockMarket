package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.BuySell.BUY;
import static com.trading.stockMarket.exchange.BuySell.SELL;
import static com.trading.stockMarket.exchange.Utils.sleepSafely;
import static org.apache.commons.lang3.ArrayUtils.toArray;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class QuoteTest {

	/**
	 * Checks whether quotes are equal to each other *
	 * 
	 */
	@Test
	public void checkQuoteEquality() {
		List<Quote> asks = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		asks.forEach(quote -> assertTrue(asks.get(0).equals(quote)));
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
		List<Quote> asks = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		Quote q1 = asks.get(0);
		sleepSafely();
		Quote q2 = new Ask("AAA", 1, 10.0);
		assertTrue(asks.contains(q1));
		assertFalse(asks.contains(q2));
	}

	/**
	 * Checks whether quotes are unequal to each other *
	 * 
	 */
	@Test
	public void checkAskAndBid() {
		List<Quote> asks = Arrays.asList(new Quote[] {QuoteFactory.askOf("AAA", 1, 10.0)});
		List<Quote> bids = Arrays.asList(new Quote[] {QuoteFactory.bidOf("AAA", 1, 10.0)});
		assertTrue(asks.size() == bids.size());
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void testWhetherTheQuotesAddUp() {
		List<Quote> asks = QuoteFactory.askOf("AAA", 2, 10.0, 10.0, 10.0);
		LongAdder askAdder = new LongAdder();// get a long adder
		asks.forEach(quote -> askAdder.add(quote.getQuantity()));
		assertTrue(askAdder.intValue() == 6);
	}

	@Test
	public void testAsksWhenUsingGenericMethod() {
		List<Quote> asks = QuoteFactory.of("ASK", toArray("AAA"), toArray(1),
				new double[][] { { 10.0, 10.0, 10.0 } });
		asks.forEach((quote) -> assertTrue(quote.buySell().equals(SELL)));
	}

	@Test
	public void testBidsWhenUsingGenericMethod() {
		List<Quote> asks = QuoteFactory.of("BID", toArray("AAA"), toArray(1),
				new double[][] { { 10.0, 10.0, 10.0 } });
		asks.forEach((quote) -> assertTrue(quote.buySell().equals(BUY)));
	}
}