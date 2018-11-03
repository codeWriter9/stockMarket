package com.trading.stockMarket.exchange;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class Order {

	private ConcurrentMap<String, List<Quote>> orders;// symbol to quote mapping
	private Lock lock;
	private String brokerId;

	/**
	 * 
	 * 
	 * 
	 */
	public Order() {
		orders = new ConcurrentHashMap<>();
		lock = new ReentrantLock();
	}
	
	
	/**
	 * 
	 * 
	 * 
	 */
	public Order(String brokerId) {
		orders = new ConcurrentHashMap<>();
		lock = new ReentrantLock();
		this.brokerId = brokerId;
	}
	
	/**
	 * 
	 * Returns the list of quotes for symbol
	 * 
	 * @param symbol
	 * @return
	 */
	public List<Quote> quotesForSymbol(String symbol) {
		return orders.get(symbol);
	}
	
	/**
	 * 
	 * Returns the Set of symbols
	 * 
	 * @return Set<String>
	 */
	public Set<String> symbols() {
		return orders.keySet();
	}
	 
	/**
	 * 
	 * Returns the list of orders
	 * 
	 * @return ConcurrentMap<String, List<Quote>>
	 */
	public ConcurrentMap<String, List<Quote>> orders() {
		return orders;
	}
	
	/**
	 * 
	 * Tests the Quotes with the predicate
	 * 
	 * @param quotePredicate
	 * @return True if all the quotes pass the test for a symbol False otherwise
	 */
	public boolean checkQuotes(String symbol, Predicate<Quote> quotePredicate) {		
		for(Quote quote : orders.get(symbol)) {
			if(!quotePredicate.test(quote)) return false;
		}
		return true;
	}
	
	
	

	/**
	 * 
	 * Adds Quote to the List of Quotes
	 * 
	 * @param order
	 * @param quote
	 */
	public void addOrder(String order, Quote quote) {
		if (quote instanceof Ask) {
			if (orders.get(order) == null) {
				synchronized (lock) {
					orders.put(order, QuoteFactory.askOf(quote));
				}
			} else {
				List<Quote> asks = orders.get(order);
				asks.add(quote);
				synchronized (lock) {
					orders.put(order, asks);
				}
			}
		} else {
			if (orders.get(order) == null) {
				synchronized (lock) {
					orders.put(order, QuoteFactory.bidOf(quote));
				}
			} else {
				List<Quote> bids = orders.get(order);
				bids.add(quote);
				synchronized (lock) {
					orders.put(order, bids);
				}
			}
		}
	}

	/**
	 * 
	 * String representation of Order object 
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		synchronized (lock) {
			orders.forEach(new BiConsumer<String, List<Quote>>() {
				public void accept(String str, List<Quote> quotes) {
					builder.append("[");
					builder.append(str);
					builder.append(" - ");
					quotes.forEach(new Consumer<Quote>() {
						@Override
						public void accept(Quote quote) {
							builder.append(quote);
						}
					});
					builder.append("],");
				};

			});
		}
		return builder.toString();
	}
}