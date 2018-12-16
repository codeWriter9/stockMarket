package com.trading.stockMarket.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
	 * Returns a List of Quotes from the Scanner
	 * 
	 * @param scanner
	 * @return List<Quote>
	 */
	public static List<Quote> of(Scanner scanner) {
		if (scanner != null) {
			List<Quote> quotes = new ArrayList<Quote>();
			Integer numberOfquotes = scanner.nextInt();
			while (numberOfquotes > 0) {
				quotes.add(of(scanner.next(), scanner.next(), scanner.nextInt(), scanner.nextDouble()));
				numberOfquotes--;
			}
			return quotes;
		} else
			throw new IllegalArgumentException("Scanner is invaid or illegal");
	}

	/**
	 * 
	 * Returns a List of Quotes based on the BUY or SELL
	 * 
	 * @param askOrBid
	 * @param symbols
	 * @param quantities
	 * @param prices
	 * @return
	 */
	public static List<Quote> of(String askOrBid, final String[] symbols, final Integer[] quantities,
			final double[][] prices) {
		if (Ask.name().equals(askOrBid)) {
			return askOf(symbols, quantities, prices);
		} else if (Bid.name().equals(askOrBid)) {
			return bidOf(symbols, quantities, prices);
		} else
			throw new IllegalArgumentException("The ask or bid argument is invaid or illegal");
	}

	/**
	 * 
	 * Returns a Quote based on the BUY or SELL
	 * 
	 * @param askOrBid
	 * @param symbol
	 * @param quantity
	 * @param price
	 * @return Quote
	 */
	public static Quote of(String askOrBid, final String symbol, final Integer quantity, final double price) {
		if (Ask.name().equals(askOrBid)) {
			return askOf(symbol, quantity, price);
		} else if (Bid.name().equals(askOrBid)) {
			return bidOf(symbol, quantity, price);
		} else
			throw new IllegalArgumentException("The ask or bid argument is invaid or illegal");
	}

	/**
	 * 
	 * Returns Ask for a symbol with unit price and quantity
	 * 
	 * @param symbol
	 * @param quantity
	 * @param price
	 * @return Ask
	 */
	public static Quote askOf(final String symbol, final Integer quantity, final double price) {
		return new Ask(symbol, quantity, price);
	}

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
	 * Returns Quote for a symbol with a unit price for a quantity
	 * 
	 * @param symbol
	 * @param quantity
	 * @param price
	 * @return Bid
	 */
	public static Quote bidOf(final String symbol, final Integer quantity, final double price) {
		return new Bid(symbol, quantity, price);
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