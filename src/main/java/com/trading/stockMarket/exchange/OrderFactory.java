package com.trading.stockMarket.exchange;

import java.util.List;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class OrderFactory {
	
	/**
	 * 
	 * 
	 * @param brokerId
	 * @param quotes
	 * @return
	 */
	public static Order order(String brokerId, List<Quote> quotes) {
		if(quotes == null || quotes.size() == 0) throw new IllegalArgumentException("Unequal Order");
		Order order = new Order(brokerId);
		for(Quote quote : quotes) {
			order.addOrder(quote.getSymbol(), quote);
		} 
		return order;
	}
}