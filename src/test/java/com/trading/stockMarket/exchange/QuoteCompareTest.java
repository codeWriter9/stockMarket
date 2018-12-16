package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.QuoteComparatorFactory.byName;
import static com.trading.stockMarket.exchange.QuoteComparatorFactory.byQuantity;
import static com.trading.stockMarket.exchange.QuoteFactory.askOf;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class QuoteCompareTest {
	
	private List<Quote> asks = new ArrayList<Quote>();
	
	@Before
	public void setup() {
		asks.addAll(askOf("AAA", 22, 1012.00, 1012.50, 1010.00));
		asks.addAll(askOf("BBB", 10, 11.90, 12.30, 19.21));
		asks.addAll(askOf("CCC", 12, 43.00, 42.19, 41.90));
	}
	
	/**
	 * Checks whether quotes are sorted by Name
	 * 
	 */
	@Test
	public void checkOrderByName() {		
		Collections.sort(asks, byName());		
		assertTrue(asks.get(0).getSymbol().equals("AAA"));
	}
	
	
	/**
	 * Checks whether quotes are sorted by Quantity
	 * 
	 */
	@Test
	public void checkOrderByQuantity() {		
		Collections.sort(asks, byQuantity());		
		assertTrue(asks.get(0).getQuantity().intValue() == 10);
	}
	
	/**
	 * Checks whether quotes are sorted by Price
	 * 
	 */
	@Test
	public void checkOrderByPrice() {		
		Collections.sort(asks, QuoteComparatorFactory.byPrice());		
		assertTrue(asks.get(0).getPrice().compareTo(11.90) == 0);
	}
}