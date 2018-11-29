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
	 * 
	 * @return BuySell.BUY
	 */
	@Override
	public BuySell buySell() {
		return BuySell.BUY;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static String name() {
		return "BID";
	}

}