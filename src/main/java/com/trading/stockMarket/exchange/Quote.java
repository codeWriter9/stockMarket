package com.trading.stockMarket.exchange;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 
 * Encapulation of a Quote which would have an unique stock symbol, unit price and quantity
 * 
 * @author Sanjay Ghosh
 *
 */
public abstract class Quote {

	protected String symbol;
	protected Integer quantity;
	protected Double price;
	private String quoteId;

	/**
	 * 
	 * 
	 * @param symbol
	 * @param quantity
	 * @param price
	 */
	public Quote(String symbol, Integer quantity, Double price) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
		this.quoteId = LocalDateTime.now().toString();
	}

	public abstract BuySell buySell();

	/**
	 * 
	 * 
	 * @return
	 */
	public synchronized String getSymbol() {
		return symbol;
	}

	/**
	 * 
	 * 
	 * @param symbol
	 */
	public synchronized void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public synchronized Integer getQuantity() {
		return quantity;
	}

	/**
	 * 
	 * 
	 * 
	 * @param quantity
	 */
	public synchronized void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public synchronized Double getPrice() {
		return price;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param price
	 */
	public synchronized void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quote) {
			Quote otherQuote = (Quote) obj;
			return symbol.equals(otherQuote.symbol) && buySell().equals(otherQuote.buySell())
					&& quantity.equals(otherQuote.quantity) && price.equals(otherQuote.price)
					&& quoteId.equals(otherQuote.quoteId);
		}
		return false;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(quoteId, symbol, quantity, price, buySell());
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public String toString() {
		return "[" + buySell() + "  " + quantity + "  " + symbol + " @ " + price + " on " + quoteId +" ]";
	}
}