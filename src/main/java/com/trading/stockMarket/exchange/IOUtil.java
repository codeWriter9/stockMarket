package com.trading.stockMarket.exchange;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * Static class to handle the input data
 * 
 * @author Sanjay Ghosh
 *
 */
public class IOUtil {

	static Scanner scanner = null;

	/**
	 * 
	 *  Static method that will reset the line to the next 
	 * 
	 */
	public static void resetToNextLine() {
		scanner().nextLine();
	}

	/**
	 * 
	 * Returns a scanner which will scan the standard input file
	 * If not found then the standard input will be take as scanner input
	 * 
	 * @return Scanner
	 */
	public static Scanner scanner() {
		if (scanner == null) {
			try {
				scanner = new Scanner(new File("Input.txt"));
			} catch (FileNotFoundException e) {
				System.err.println("Unable to find the input File. Will take Standard Input as Input File");
				scanner = new Scanner(System.in);
			}
		}
		return scanner;
	}

	/**
	 * 
	 * Returns a scanner which will scan the provided input file
	 * If not found then the standard input will be take as scanner input
	 * 
	 * @param String
	 *            inputFile
	 * @return Scanner
	 */
	public static Scanner scanner(String inputFile) {
		if (scanner == null) {
			try {
				scanner = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				System.err.println("Unable to find the input File. Will take Standard Input as Input File");
				scanner = new Scanner(System.in);
			}
		}
		return scanner;
	}
	
	/**
	 * 
	 * Close and Release the scanner
	 * 
	 */
	public static void closeAndRelease() {
		if (scanner != null) scanner.close();
		scanner = null;
	}
}