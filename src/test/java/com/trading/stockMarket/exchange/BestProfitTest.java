package com.trading.stockMarket.exchange;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * 
 * Test class for checking the most profitable case scenarios
 * 
 * @author Sanjay Ghosh
 *
 */
public class BestProfitTest {
	
	protected static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Checks the historical data for prices and gives you two dates on which you
	 * could have bought them to maximize your profits
	 * 
	 */
	@Test
	public void profitFromHistoricalData() {
		List<Quote> asks = QuoteFactory.askOf("AAA", 1, 10.0, 11.0, 12.0, 9.0, 10.2, 11.2);
		List<Quote> bids = QuoteFactory.bidOf("AAA", 1, 9.5, 10.5, 11.5, 8.5, 9.8, 10.8);

		int buyDate = 0;
		int sellDate = 0;
		double profit = Double.MIN_VALUE;
		for (int i = 0; i < bids.size(); i++) {
			for (int j = i + 1; j < asks.size(); j++) {
				if (asks.get(j).getPrice() - bids.get(i).getPrice() > profit) {
					profit = asks.get(j).getPrice() - bids.get(i).getPrice();
					buyDate = i + 1;
					sellDate = j + 1;
				}
			}
		}
		LOGGER.info(" Profit is largest at  " + profit + " buy on  " + buyDate + " and sell on  " + sellDate);
		assertTrue(buyDate == 4);
		assertTrue(sellDate == 6);
		assertTrue(new Double(profit).equals(new Double(2.6999999999999993)));
	}

}