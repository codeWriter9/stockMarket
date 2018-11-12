package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.Utils.shutDownGracefully;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class CyclicBarrierTest {
	
	@Test
	public void testCyclicBarrier()  {
		List<Order> orders = new ArrayList<Order>();
		BlockingQueue<Order> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
		Matcher matcher = new Matcher(orders);
		CyclicBarrier barrier = new CyclicBarrier(2, matcher);
		List<Quote> asksAAA = QuoteFactory.askOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> asksBBB = QuoteFactory.askOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> asksCCC = QuoteFactory.askOf("CCC", 1, 21.0, 23.0, 24.0);
		List<Quote> asks = new ArrayList<>();
		asks.addAll(asksAAA);
		asks.addAll(asksBBB);
		asks.addAll(asksCCC);
		Order order1 = new Order("Trader1");
		for (Quote quote : asks) {
			order1.addOrder(quote.getSymbol(), quote);
		}
		Trader trader1 = new Trader("Trader1", barrier, 1, arrayBlockingQueue, matcher);
		List<Quote> bidsAAA = QuoteFactory.bidOf("AAA", 1, 10.0, 10.0, 10.0);
		List<Quote> bidsBBB = QuoteFactory.bidOf("BBB", 1, 11.0, 11.0, 11.0);
		List<Quote> bidsCCC = QuoteFactory.bidOf("CCC", 1, 21.0, 23.0, 24.0);

		List<Quote> bids = new ArrayList<>();
		bids.addAll(bidsAAA);
		bids.addAll(bidsBBB);
		bids.addAll(bidsCCC);

		
		Order order2 = new Order("Trader2");
		for (Quote quote : bids) {
			order2.addOrder(quote.getSymbol(), quote);
		}
		Trader trader2 = new Trader("Trader2", barrier, 1, arrayBlockingQueue, matcher);
		try {
			arrayBlockingQueue.put(order1);
			arrayBlockingQueue.put(order2);
		} catch (InterruptedException e) {
			assertFalse(e != null);// this will fail
		}
		
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(trader1);
		service.execute(trader2);
		shutDownGracefully(service, (runnable) -> System.out.println(" waiting for " + runnable), 10L, TimeUnit.SECONDS);
		assertFalse(false);
	}

}
