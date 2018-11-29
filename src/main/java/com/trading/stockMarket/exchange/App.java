package com.trading.stockMarket.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	protected static final Logger LOGGER = LogManager.getLogger();


    public static void main( String[] args )
    {
    	LOGGER.info( "Hello World!" );
    	Exchange exchange = new Exchange();
    	exchange.execute();
    	exchange.shutDown();
    }
}
