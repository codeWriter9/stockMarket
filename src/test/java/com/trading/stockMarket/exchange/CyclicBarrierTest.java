package com.trading.stockMarket.exchange;

import static com.google.common.collect.Lists.newArrayList;
import static com.trading.stockMarket.exchange.OrderFactory.order;
import static com.trading.stockMarket.exchange.QuoteFactory.askOf;
import static com.trading.stockMarket.exchange.QuoteFactory.bidOf;
import static com.trading.stockMarket.exchange.Utils.blockingQueue;
import static com.trading.stockMarket.exchange.Utils.shutDownGracefully;
import static java.lang.System.out;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

import org.junit.Test;

public class CyclicBarrierTest {

	/**
	 * 
	 * 
	 * 
	 * 
	 */
	@Test
	public void testCyclicBarrier() {

		Order order1 = order("Trader1", askOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		Order order2 = order("Trader2", bidOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		List<Order> orders = newArrayList(order1, order2);
		Matcher matcher = new Matcher(orders);
		CyclicBarrier barrier = new CyclicBarrier(2, matcher);

		BlockingQueue<Order> arrayBlockingQueue = blockingQueue(orders);
		Trader trader1 = new Trader("Trader1", barrier, 1, arrayBlockingQueue, matcher);
		Trader trader2 = new Trader("Trader2", barrier, 1, arrayBlockingQueue, matcher);
		ExecutorService service = newCachedThreadPool();
		service.execute(trader1);
		service.execute(trader2);
		shutDownGracefully(service, (runnable) -> out.println(" waiting for " + runnable), 10L, SECONDS);

		for (String symbol : matcher.askOrder().symbols())
			assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		for (String symbol : matcher.bidOrder().symbols())
			assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
	}
	
	
	@Test
	public void testCyclicBarrier2() {

		Order order1 = order("Trader1", askOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		Order order2 = order("Trader2", bidOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		List<Order> orders = newArrayList(order1, order2);
		Matcher matcher = new Matcher(orders);
		CyclicBarrier barrier = new CyclicBarrier(2, matcher);

		BlockingQueue<Order> arrayBlockingQueue = blockingQueue(orders);
		Trader trader1 = new Trader("Trader1", barrier, 2, arrayBlockingQueue, matcher);
		Trader trader2 = new Trader("Trader2", barrier, 2, arrayBlockingQueue, matcher);
		ExecutorService service = newCachedThreadPool();
		service.execute(trader1);
		service.execute(trader2);
		Utils.sleepSafely(1000);
		barrier.reset();
		for (String symbol : matcher.askOrder().symbols())
			assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		for (String symbol : matcher.bidOrder().symbols())
			assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		
		Utils.sleepSafely(1000);
		order1 = order("Trader1", askOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		order2 = order("Trader2", bidOf(new String[] { "AAA", "BBB", "CCC" }, new Integer[] { 1, 1, 1 },
				new double[][] { { 10.0, 10.0, 10.0 }, { 11.0, 11.0, 11.0 }, { 21.0, 23.0, 24.0 } }));
		orders = newArrayList(order1, order2);
		matcher.clear();// We cannot create a new Matcher as the Cyclic Barrier has already been made with this instance
		arrayBlockingQueue = blockingQueue(orders);
		service.execute(trader1);
		service.execute(trader2);
		Utils.sleepSafely(1000);
		for (String symbol : matcher.askOrder().symbols())
			assertTrue(matcher.askOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		for (String symbol : matcher.bidOrder().symbols())
			assertTrue(matcher.bidOrder().checkQuotes(symbol, (quote) -> quote.getQuantity() == 0));
		shutDownGracefully(service, (runnable) -> out.println(" waiting for " + runnable), 10L, SECONDS);
	}
}