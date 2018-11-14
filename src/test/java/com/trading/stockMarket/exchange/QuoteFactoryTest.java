package com.trading.stockMarket.exchange;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class QuoteFactoryTest {
	
	/**
	 * Checks whether quotes are equal to each other *
	 * 
	 */
	@Test
	public void checkBulkQuotesAcrossSymbols() {
		List<Quote> asks = QuoteFactory.askOf(new String []{"AAA", "BBB", "CCC"}, new Integer[] {1, 1, 1}, new double[][] {{10.0, 10.0, 10.0}, {11.0, 11.0, 11.0}, {21.0, 23.0, 24.0}});		
		for(Quote ask : asks) {
			if(ask.getSymbol().equals("AAA")) assertTrue(ask.price == 10.0);
			if(ask.getSymbol().equals("BBB")) assertTrue(ask.price == 11.0);
		}
		List<Quote> bids = QuoteFactory.bidOf(new String []{"AAA", "BBB", "CCC"}, new Integer[] {1, 1, 1}, new double[][] {{10.0, 10.0, 10.0}, {11.0, 11.0, 11.0}, {21.0, 23.0, 24.0}});
		for(Quote bid : bids) {
			if(bid.getSymbol().equals("AAA")) assertTrue(bid.price == 10.0);
			if(bid.getSymbol().equals("BBB")) assertTrue(bid.price == 11.0);
		}
	}
}