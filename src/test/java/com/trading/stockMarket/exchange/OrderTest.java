package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.Utils.shutDownGracefully;
import static com.trading.stockMarket.exchange.Utils.sleepSafely;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class OrderTest {

	/**
	 * Checks whether quotes are equal to each other
	 * 
	 */
	@Test
	public void checkOrderEquality() {
		List<Quote> asks = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		sleepSafely();
		Order order = new Order("Trader1");

		order.addOrder("AAA", asks.get(0));
		order.addOrder("AAA", new Ask("AAA", 1, 10.0));

		assertTrue((order.checkQuotes("AAA", (quote) -> quote.getPrice() == 10.0)));
		asks.forEach((quote) -> quote.setPrice(quote.getPrice() + 1.0));// change a quote in the ask list but not
																		// directly
		assertFalse((order.checkQuotes("AAA", (quote) -> quote.getPrice() == 11.0)));
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void checkOrders() {
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);
		sleepSafely();
		List<Quote> quotes = new ArrayList<>();
		quotes.addAll(asksAAA);
		quotes.addAll(asksBBB);
		quotes.addAll(asksCCC);
		Order order = new Order("Trader1");

		// add all orders
		for (Quote quote : quotes) {
			order.addOrder(quote.getSymbol(), quote);
		}

		for (String symbol : order.symbols()) {
			if ("AAA".equals(symbol))
				order.quotesForSymbol(symbol).forEach(quote -> assertTrue(quote.getPrice().equals(10.0)));
			else if ("BBB".equals(symbol))
				order.quotesForSymbol(symbol).forEach(quote -> assertTrue(quote.getPrice().equals(11.0)));
		}

	}

	@Test
	public void checkTwoOrdersEvenlyMatched() {
		List<Order> orders = new ArrayList<Order>();
		Matcher matcher = new Matcher(orders);

		System.out.println(" running checkTwoOrders:EvenlyMatched ");
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running checkTwoOrders:EvenlyMatched: Adding Order 1 ");
		Order order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running checkTwoOrders:EvenlyMatched: Adding Order 2 ");
		Order order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running checkTwoOrders:EvenlyMatched: starting Matcher ");		
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
	}
	
	
	@Test
	public void checkTwoOrdersAsksOutMatchingBids() {
		List<Order> orders = new ArrayList<Order>();
		Matcher matcher = new Matcher(orders);

		System.out.println(" running checkTwoOrders:AsksOutMatchingBids ");
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 2, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 2, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 2, 21.0, 23.0, 24.0);

		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running checkTwoOrders:AsksOutMatchingBids: Adding Order 1 ");
		Order order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running checkTwoOrders:AsksOutMatchingBids: Adding Order 2 ");
		Order order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running checkTwoOrders:AsksOutMatchingBids: starting Matcher ");		
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 3));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
	}
	
	
	@Test
	public void checkTwoOrdersBidsOutMatchingAks() {
		List<Order> orders = new ArrayList<Order>();
		Matcher matcher = new Matcher(orders);

		System.out.println(" running checkTwoOrders:BidsOutMatchingAsks ");
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running checkTwoOrders:BidsOutMatchingAsks: Adding Order 1 ");
		Order order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 2, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 2, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 2, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running checkTwoOrders:BidsOutMatchingAsks: Adding Order 2 ");
		Order order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running checkTwoOrders:BidsOutMatchingAsks: starting Matcher ");		
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 3));
	}
	
	
	
	
	@Test
	public void test3ScenariosOnASingleMatcher() {
		List<Order> orders = new ArrayList<Order>();
		Matcher matcher = new Matcher(orders);

		System.out.println(" running 3ScenariosOnASingleMatcher ");
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 1 ");
		Order order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 2 ");
		Order order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running 3ScenariosOnASingleMatcher: starting Matcher ");		
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		
		clear(orders, matcher, asksAAA, asksBBB, asksCCC, asks, bidsAAA, bidsBBB, bidsCCC, bids);
		
		orders = new ArrayList<Order>();
		matcher = new Matcher(orders);

		System.out.println(" running 3ScenariosOnASingleMatcher ");
		asksAAA = QuoteFactory.askOf("AAA", 2, 10.0, 10.0, 10.0);
		asksBBB = QuoteFactory.askOf("BBB", 2, 11.0, 11.0, 11.0);
		asksCCC = QuoteFactory.askOf("CCC", 2, 21.0, 23.0, 24.0);

		asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 1 ");
		order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 2 ");
		order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running 3ScenariosOnASingleMatcher: starting Matcher ");		
		service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 3));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		
		clear(orders, matcher, asksAAA, asksBBB, asksCCC, asks, bidsAAA, bidsBBB, bidsCCC, bids);
		
		
		orders = new ArrayList<Order>();
		matcher = new Matcher(orders);

		System.out.println(" running 3ScenariosOnASingleMatcher ");
		asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);

		asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 1 ");
		order1 = new Order("Bull");
		// add all orders
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}

		bidsAAA = QuoteFactory.bidOf("AAA", 2, 10.0, 10.0, 10.0);
		bidsBBB = QuoteFactory.bidOf("BBB", 2, 11.0, 11.0, 11.0);
		bidsCCC = QuoteFactory.bidOf("CCC", 2, 21.0, 23.0, 24.0);

		bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		System.out.println(" running 3ScenariosOnASingleMatcher: Adding Order 2 ");
		order2 = new Order("Bear");
		// add all orders
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}

		orders.add(order1);
		orders.add(order2);
		System.out.println(" running 3ScenariosOnASingleMatcher: starting Matcher ");		
		service = Executors.newCachedThreadPool();
		service.execute(matcher);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable));		
		for(String symbol :matcher.askOrder().symbols()) assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));		
		for(String symbol :matcher.bidOrder().symbols()) assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 3));
	}

	/**
	 * 
	 * 
	 * 
	 * @param orders
	 * @param matcher
	 * @param asksAAA
	 * @param asksBBB
	 * @param asksCCC
	 * @param asks
	 * @param bidsAAA
	 * @param bidsBBB
	 * @param bidsCCC
	 * @param bids
	 */
	private void clear(List<Order> orders, Matcher matcher, List<Quote> asksAAA, List<Quote> asksBBB,
			List<Quote> asksCCC, List<Quote> asks, List<Quote> bidsAAA, List<Quote> bidsBBB, List<Quote> bidsCCC,
			List<Quote> bids) {
		orders.clear();
		matcher.clear();
		
		asksAAA.clear();
		asksBBB.clear();
		asksCCC.clear();		
		asks.clear();
		
		bidsAAA.clear();
		bidsBBB.clear();	
		bidsCCC.clear();
		bids.clear();
	}
}