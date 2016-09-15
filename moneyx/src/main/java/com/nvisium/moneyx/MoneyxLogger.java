package com.nvisium.moneyx;

//import java.io.IOException;
//import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyxLogger {
	private static final Logger LOGGER = Logger.getLogger(MoneyxLogger.class.getName());
	
	public static void log(String msg) {
		LOGGER.info(msg);
	}
}
