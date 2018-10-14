package com.trading.stockMarket.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * 
 * Utility class to build quotes both bid and ask
 * 
 * @author Sanjay Ghosh
 *
 */
public class QuoteBuilder {

	/**
	 * 
	 * Returns the list of Asks for a symbol with multiple unit prices for the same quantity 
	 * 
	 * @param symbol
	 * @param quantity
	 * @param prices
	 * @return list of asks
	 */
	public static List<Quote> askOf(final String symbol, final Integer quantity, final double... prices) {
		List<Quote> asks = new ArrayList<Quote>();
		DoubleStream.of(prices).forEach((price) -> asks.add(new Ask(symbol, quantity, price)));
		return asks;
	}
	
	/**
	 * 
	 * Returns the list of Bids for a symbol with multiple unit prices for the same quantity
	 * 
	 * @param symbol
	 * @param quantity
	 * @param prices
	 * @return list of bids
	 */
	public static List<Quote> bidOf(final String symbol, final Integer quantity, final double... prices) {
		List<Quote> bids = new ArrayList<Quote>();
		DoubleStream.of(prices).forEach((price) -> bids.add(new Bid(symbol, quantity, price)));
		return bids;
	}

}