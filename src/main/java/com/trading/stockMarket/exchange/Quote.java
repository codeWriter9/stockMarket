package com.trading.stockMarket.exchange;

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
	}

	public abstract BuySell buySell();

	/**
	 * 
	 * 
	 * @return
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * 
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 
	 * 
	 * 
	 * @param quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param price
	 */
	public void setPrice(Double price) {
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
					&& quantity.equals(otherQuote.quantity) && price.equals(otherQuote.price);
		}
		return super.equals(obj);
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(symbol, quantity, price, buySell());
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public String toString() {

		return "[" + buySell() + "  " + quantity + "  " + symbol + " @ " + price + " ]";
	}
}