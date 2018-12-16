package com.trading.stockMarket.exchange;

/**
 * 
 * Buy Quote
 * 
 * @author Sanjay Ghosh
 *
 */
public class Bid extends Quote {
	
	/**
	 * 
	 * public constructor for Bid
	 * 
	 * @param symbol
	 * @param quantity
	 * @param price
	 */
	public Bid(String symbol, Integer quantity, Double price) {
		super(symbol, quantity, price);
	}

	/**
	 * 
	 * Returns the BuySell bid or Buy direction
	 * 
	 * @return BuySell.BUY
	 */
	@Override
	public BuySell buySell() {
		return BuySell.BUY;
	}
	
	/**
	 * 
	 * Returns BID
	 * 
	 * @return String
	 */
	public static String name() {
		return "BID";
	}

}