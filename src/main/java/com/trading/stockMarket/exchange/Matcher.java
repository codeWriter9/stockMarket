package com.trading.stockMarket.exchange;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * 
 * Matcher - This class matches the Orders passed to it as per quotes. If there
 * quotes for both directions they will get cancelled out
 * 
 * @author Sanjay Ghosh
 *
 */
public class Matcher implements Runnable {

	private ConcurrentMap<String, Quote> orders;// Map of broker id to Quotes Current Support only one stock per broker
												// id this would be enhanced later
	private ConcurrentMap<String, Quote> askMap;// symbol to ask map
	private ConcurrentMap<String, Quote> bidMap;// symbol to bid map
	private Set<String> unmatchedSymbols;
	private Lock lock;
	
	
	
	

	/**
	 * 
	 * Controls the while loop inside the run method
	 * 
	 * @return True when the matcher has run to the best of the ability
	 */
	private boolean done() {
		return Collections.unmodifiableSet(unmatchedSymbols).size() == 0;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void run() {
		while(!done()) {
			
		}
	}

}
