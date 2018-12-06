package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.IOUtil.scanner;
import static com.trading.stockMarket.exchange.Utils.shutDownGracefully;
import static com.trading.stockMarket.exchange.Utils.sleepSafely;
import static java.lang.System.out;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.LongAdder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class Exchange {
	
	protected static final Logger LOGGER = LogManager.getLogger();
	
	private Trader [] traders;
	private Scanner scanner;
	private List<Order> orders = new ArrayList<Order>();
	private Matcher matcher = new Matcher(orders);
	private CyclicBarrier barrier;
	private BlockingQueue<Order> arrayBlockingQueue;
	private ExecutorService service = newCachedThreadPool();

	
	/**
	 * 
	 * 
	 * 
	 */
	public Exchange() {
		scanner = scanner();
		buildTraders();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param inputFile
	 */
	public Exchange(String inputFile) {
		scanner = scanner(inputFile);
		buildTraders();
	}
	
	
	/**
	 * 
	 * 
	 */
	public void execute() {
		for(Trader trader : traders) {
			service.execute(trader);
		}
		sleepSafely(1000);
	}
	
	/**
	 * 
	 * 
	 */
	public void reset() {
		barrier.reset();		
		matcher.clear();// We cannot create a new Matcher as the Cyclic Barrier has already been made with this instance
		Utils.sleepSafely(1000);
		buildTraders();
	}
	
	/**
	 * 
	 * 
	 */
	public void shutDown() {
		shutDownGracefully(service, (runnable) -> out.println(" waiting for " + runnable), 10L, SECONDS);
	}
	
	/**
	 * 
	 * 
	 */
	public Status status() {
		LongAdder adder = new LongAdder();
		for(String symbol :matcher.askOrder().symbols()) adder.add(matcher.askOrder().quotesForSymbol(symbol).size());
		for(String symbol :matcher.bidOrder().symbols()) adder.add(-matcher.bidOrder().quotesForSymbol(symbol).size());
		if(adder.longValue() > 0) return Status.OVERASK;
		else if (adder.longValue() > 0) return Status.OVERBID;
		else return Status.RECONCILED;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private void buildTraders() {
		Integer numberOfTraders =scanner.nextInt(); 
		traders = new Trader[numberOfTraders];
		barrier = new CyclicBarrier(numberOfTraders, matcher);
		arrayBlockingQueue = new ArrayBlockingQueue<>(numberOfTraders);
		for(int i=0;i<traders.length;i++) {
			traders[i] = toTrader(traders.length);
		}
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param numberOfTraders
	 * @return Trader
	 */
	private Trader toTrader(Integer numberOfTraders) {
		Order order = toOrder();
		orders.add(order);
		return new Trader(order.brokerId(), barrier, 2, arrayBlockingQueue, matcher);
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return List<Quote>
	 */
	private List<Quote> toListOfQuotes() {
		return QuoteFactory.of(scanner);
	}
	
	/**
	 * 
	 * 
	 * @return Order
	 */
	private Order toOrder() {
		Order order = OrderFactory.of(scanner.next(), toListOfQuotes());
		try {
			arrayBlockingQueue.put(order);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return order;
	}
}