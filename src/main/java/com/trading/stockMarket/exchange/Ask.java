package com.trading.stockMarket.exchange;

/**
 * 
 * Sell Quote
 * 
 * @author Sanjay Ghosh
 *
 */
public class Ask extends Quote {

	/**
	 * 
	 * public constructor for Ask
	 * 
	 * @param symbol
	 * @param quantity
	 * @param price
	 */
	public Ask(String symbol, Integer quantity, Double price) {
		super(symbol, quantity, price);
	}

	/**
	 * 
	 * 
	 * @return BuySell.SELL
	 */
	@Override
	public BuySell buySell() {
		return BuySell.SELL;
	}
	
	/**
	 * 
	 * Returns BID
	 * 
	 * @return String
	 */
	public static String name() {
		return "ASK";
	}
}