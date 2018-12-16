package com.trading.stockMarket.exchange;

import static java.util.Comparator.comparing;

import java.util.Comparator;

/**
 * 
 * Returns various types of Quote Factories
 * 
 * @author Sanjay Ghosh
 *
 */
public class QuoteComparatorFactory {

	/**
	 * 
	 * Returns a Quote Comparator which compares by name
	 * 
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byName() {
		return comparing(Quote::getSymbol);
	}

	/**
	 * 
	 * Returns a Quote Comparator which compares by price
	 * 
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byPrice() {
		return comparing(Quote::getPrice);
	}

	/**
	 * 
	 * Returns a Quote Comparator which compares by quantity
	 * 
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byQuantity() {
		return comparing(Quote::getQuantity);
	}

	/**
	 * 
	 * Returns a Quote Comparator which compares by name
	 * 
	 * @param Comparator<Quote>
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byName(Comparator<Quote> comparator) {
		return comparator.thenComparing(Quote::getSymbol);
	}

	/**
	 * 
	 * Returns a Quote Comparator which compares by price
	 * 
	 * @param Comparator<Quote>
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byPrice(Comparator<Quote> comparator) {
		return comparator.thenComparing(Quote::getPrice);
	}

	/**
	 * 
	 * Returns a Quote Comparator which compares by quantity
	 * 
	 * @param Comparator<Quote>
	 * @return Comparator<Quote>
	 */
	public static Comparator<Quote> byQuantity(Comparator<Quote> comparator) {
		return comparator.thenComparing(Quote::getQuantity);
	}
}