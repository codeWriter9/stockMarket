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
public class QuoteFactory {

	/**
	 * 
	 * Returns the list of Asks for a symbol with multiple unit prices for the same
	 * quantity
	 * 
	 * @param quote
	 * @return list of asks
	 */
	public static List<Quote> askOf(final Quote quote) {
		List<Quote> asks = new ArrayList<Quote>();
		asks.add(quote);
		return asks;
	}

	/**
	 * 
	 * Returns the list of Asks for a symbol with multiple unit prices for the same
	 * quantity
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
	 * Returns the list of Asks for multiple symbols with multiple unit prices for
	 * the given quantity of symbols
	 * 
	 * @param symbol
	 * @param quantity
	 * @param prices
	 * @return list of asks
	 */
	public static List<Quote> askOf(final String[] symbols, final Integer[] quantities, final double[][] prices) {
		List<Quote> asks = new ArrayList<Quote>();
		for (int index = 0; index < symbols.length; index++)
			asks.addAll(askOf(symbols[index], quantities[index], prices[index]));
		return asks;
	}

	/**
	 * 
	 * Returns the list of Bids for a symbol with multiple unit prices for the same
	 * quantity
	 * 
	 * @param quote
	 * @return list of bids
	 */
	public static List<Quote> bidOf(final Quote quote) {
		List<Quote> bids = new ArrayList<Quote>();
		bids.add(quote);
		return bids;
	}

	/**
	 * 
	 * Returns the list of Bids for a symbol with multiple unit prices for the same
	 * quantity
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

	/**
	 * 
	 * Returns the list of Bids for multiple symbols with multiple unit prices for
	 * the given quantity of symbols
	 * 
	 * @param symbol
	 * @param quantity
	 * @param prices
	 * @return list of bids
	 */
	public static List<Quote> bidOf(final String[] symbols, final Integer[] quantities, final double[][] prices) {
		List<Quote> bids = new ArrayList<Quote>();
		for (int index = 0; index < symbols.length; index++)
			bids.addAll(bidOf(symbols[index], quantities[index], prices[index]));

		return bids;
	}

}