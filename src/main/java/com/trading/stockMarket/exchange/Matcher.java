package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.QuoteFactory.askOf;
import static com.trading.stockMarket.exchange.QuoteFactory.bidOf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Matcher - This class matches the Orders passed to it as per quotes. If there
 * quotes for both directions they will get cancelled out
 * 
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class Matcher implements Runnable {

	private List<Order> orders;
	private Order askOrder;// ask Order
	private Order bidOrder;// bid Order
	private Set<String> unmatchedSymbols;
	private Lock lock;
	private Condition notReady;

	/**
	 * 
	 * Constructor where you pass a List<Order> of Orders.
	 * 
	 * @param orders
	 */
	public Matcher(List<Order> orders) {
		this.orders = orders;
		initialize();
		allOrdersAdded();
	}

	/**
	 * 
	 * Return an ask Order
	 * 
	 * @return Order
	 */
	public Order askOrder() {
		return askOrder;
	}

	/**
	 * 
	 * Return an bid Order
	 * 
	 * @return Order
	 */
	public Order bidOrder() {
		return bidOrder;
	}

	/**
	 * 
	 * Default Constructors
	 * 
	 */
	public Matcher() {
		this.orders = new ArrayList<>();
		initialize();
	}
	
	
	public void clear() {
		nullify();
		this.orders = new ArrayList<>();
		initialize();
	}

	/**
	 * 
	 * Initialize the locks, orders, matched symbols and the condition
	 * 
	 */
	private void initialize() {
		this.lock = new ReentrantLock();
		this.askOrder = new Order("Ask");
		this.bidOrder = new Order("Bid");
		this.unmatchedSymbols = new LinkedHashSet<String>();
		this.notReady = lock.newCondition();// get a new condition
	}
	
	
	/**
	 * 
	 * 
	 */
	private void nullify() {
		this.orders = null;
		this.lock = null;
		this.askOrder = null;
		this.bidOrder = null;
		this.unmatchedSymbols = null;
		this.notReady = null;
	}

	/**
	 * 
	 * Add an Order
	 * 
	 * @param order
	 */
	public synchronized void addOrder(Order order) {
		orders.add(order);
	}

	/**
	 * 
	 * Notify when we are ready to start
	 * 
	 */
	private void allOrdersAdded() {
		lock.lock();
		try {
			notReady.signal();
		} catch (Exception e) {
			System.err.println(" while signalling all orders are ready " + e.getMessage());
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 
	 * This method should run until all the symbols in the unmatchedSymbols are
	 * matched
	 * 
	 */
	private void reconcile() {
//		 System.out.println(" reconcile ");
		synchronized (lock) { // Take a lock
			for (Iterator<String> itr = unmatchedSymbols.iterator(); itr.hasNext();) { // while there are still
																						// unmatched symbols
				String symbol = itr.next();				
				List<Quote> askList = askOrder.quotesForSymbol(symbol);// ask list
				List<Quote> bidList = bidOrder.quotesForSymbol(symbol);// bid list
				double askPrice = askList.get(0).price;// ask price
				double bidPrice = bidList.get(0).price;// bid price
				LongAdder askAdder = new LongAdder();// get a long adder
				LongAdder bidAdder = new LongAdder();// get a long adder
				askList.forEach((quote) -> askAdder.add(quote.getQuantity()));// add number of ask price to a long adder
				bidList.forEach((quote) -> bidAdder.add(quote.getQuantity()));// add number of bid price to a long adder
				askList.clear();
				bidList.clear();
				if (askAdder.longValue() >= bidAdder.longValue()) {
					askList.add(askOf(symbol, (int) (askAdder.longValue() - bidAdder.longValue()), askPrice));
					bidList.add(bidOf(symbol, 0, bidPrice));
				} else {
					askList.add(askOf(symbol, 0, askPrice));
					bidList.add(bidOf(symbol, (int) (bidAdder.longValue() - askAdder.longValue()), bidPrice));
				}				
			}
		}
	}

	/**
	 * 
	 * 
	 * (1) Check if all orders are loaded if yes sort them into ask and bid (2) If
	 * not loaded get a lock and wait
	 * 
	 */
	private void setOrders() {
//		System.out.println("set Orders");
		if (orders != null && !orders.isEmpty()) {
			synchronized (lock) { // Take a lock
				for (Order order : orders) { // for every broker collect the order
					for (String orderList : order.orders().keySet()) { // get the List of Orders of symbol
						List<Quote> quotes = order.orders().get(orderList);
						quotes.forEach((quote) -> {
							if (quote instanceof Ask)
								askOrder.addOrder(quote.getSymbol(), quote);// add to ask orders
							else
								bidOrder.addOrder(quote.getSymbol(), quote); // add to bid orders
							unmatchedSymbols.add(quote.getSymbol());// add symbol to symbol list
						});
					}
				}
			}
		} else { // If not set then an ask Order
			lock.lock();
			try {
				notReady.await();
			} catch (InterruptedException e) {
				System.err.println(" while waiting to set order " + e.getCause());
			} finally {
				lock.unlock();
			}

		}

	}

	/**
	 * 
	 * Standard Run Method But given it is doing two tasks So broken to two separate
	 * methods
	 * 
	 */
	@Override
	public void run() {
		setOrders();// set orders and if not set then wait
		reconcile();// reconcile for all that have been set
	}

	/**
	 * 
	 * String representation of the Object
	 * 
	 */
	@Override
	public String toString() {
		String str;
		lock.lock();
		str = "Matcher->" + orders.toString();
		lock.unlock();
		return str;
	}
}