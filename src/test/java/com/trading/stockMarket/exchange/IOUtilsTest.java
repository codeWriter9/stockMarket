package com.trading.stockMarket.exchange;

import static com.trading.stockMarket.exchange.IOUtil.closeAndRelease;
import static com.trading.stockMarket.exchange.IOUtil.scanner;
import static com.trading.stockMarket.exchange.OrderFactory.of;
import static com.trading.stockMarket.exchange.QuoteFactory.of;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class IOUtilsTest {

	@Test
	public void test() {
		Scanner scanner = scanner();
		Integer numberOfTraders = scanner.nextInt();
		assertTrue(numberOfTraders == 2);
		String nameOfTrader = scanner.next();
		System.out.println(nameOfTrader);
		System.out.println(QuoteFactory.of(scanner));
		nameOfTrader = scanner.next();
		System.out.println(nameOfTrader);
		System.out.println(QuoteFactory.of(scanner));
		IOUtil.closeAndRelease();
	}
	
	@Test
	public void test2() {
		Scanner scanner = scanner();
		Integer numberOfTraders = scanner.nextInt();
		List<Order> orders = new ArrayList<Order>();
		while(numberOfTraders > 0) {
			orders.add(of(scanner.next(), of(scanner)));
			numberOfTraders--;
		}
		System.out.println(orders);
		closeAndRelease();
	}
}