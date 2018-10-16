package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.QuoteTest.sleepSafely;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

public class OrderTest {
	
	/**
	 * Checks whether quotes are equal to each other *
	 * 
	 */
	@Test
	public void checkOrderEquality() {
		List<Quote> asks = QuoteBuilder.askOf("AAA", 1, 10.0, 10.0, 10.0);
		sleepSafely();
		Order order = new Order();
		order.addOrder("trader1", asks.get(0));
		order.addOrder("trader1", new Ask("AAA", 1, 10.0));		
		System.out.println(order);		
		asks.forEach(new Consumer<Quote>() {
			@Override
			public void accept(Quote quote) {			
				quote.setPrice(quote.getPrice() + 1.0);
			}
		});		
		System.out.println(order);
	}

}
