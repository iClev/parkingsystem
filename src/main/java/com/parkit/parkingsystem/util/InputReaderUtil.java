package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Class to capture the the user entry
 *
 * @author Clévyd
 */
public class InputReaderUtil {

private static final Logger logger = LogManager.getLogger("InputReaderUtil");
private static final Scanner scan = new Scanner(System.in, "UTF-8");

/**
 * Method to read the selection menu
 *
 * @return input the number from user or -1 if it doesn't read the entry
 */
public int readSelection() {
	try {
		final int input = Integer.parseInt(scan.nextLine());
		return input;
	} catch (final Exception e) {
		logger.error("Error while reading user input from Shell", e);
		logger.error("Error reading input. Please enter valid number for proceeding further", e);
		return -1;
	}
}

/**
 * Method to capture the vehicle registration number
 *
 * @return vehicleRegNumber
 * @throws Exception
 */
public String readVehicleRegistrationNumber() throws Exception {
	try {
		final String vehicleRegNumber = scan.nextLine();
		if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
			throw new IllegalArgumentException("Invalid input provided");
		}
		return vehicleRegNumber;
	} catch (final Exception e) {
		logger.error("Error while reading user input from Shell", e);
		logger.error("Error reading input. Please enter a valid string for vehicle registration number", e);
		throw e;
	}
}
}
