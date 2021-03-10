package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Class to contain services of parking
 *
 * @author Clévyd
 */
public class ParkingService {

private static final Logger logger = LogManager.getLogger("ParkingService");
private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();

private final InputReaderUtil inputReaderUtil;
private final ParkingSpotDAO parkingSpotDAO;
private final TicketDAO ticketDAO;

/**
 * @param inputReaderUtil
 * @param parkingSpotDAO
 * @param ticketDAO
 */
public ParkingService(final InputReaderUtil inputReaderUtil, final ParkingSpotDAO parkingSpotDAO, final TicketDAO ticketDAO) {
	this.inputReaderUtil = inputReaderUtil;
	this.parkingSpotDAO = parkingSpotDAO;
	this.ticketDAO = ticketDAO;
}

/**
 * Method to capture informations, to check and to save ticket in DB
 */
public void processIncomingVehicle() {
	try {
		final ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
		if (parkingSpot != null && parkingSpot.getId() > 0) {
			final String vehicleRegNumber = getVehichleRegNumber();
			parkingSpot.setAvailable(false);
			parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false
			
			final Date inTime = new Date();
			final Ticket ticket = new Ticket();
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber(vehicleRegNumber);
			ticket.setPrice(0);
			ticket.setInTime(inTime);
			ticket.setOutTime(null);
			ticketDAO.saveTicket(ticket);
			
			final boolean recurring = ticketDAO.isAlreadyClient(vehicleRegNumber);
			if (recurring) {
				logger.info("Welcome back! As a recurring" + "user of our parking lot," + "you'll benefit from a 5% discount");
			}
			logger.info("Generated Ticket and saved in DB");
			logger.info("Please park your vehicle in spot number: {}", parkingSpot.getId());
			logger.info("Recorded in-time for vehicle number: {} is: {}", vehicleRegNumber, inTime);
			
		}
	} catch (final Exception e) {
		logger.error("Unable to process incoming vehicle", e);
	}
}

/**
 * @return vehicle registration number enter by user
 * @throws Exception
 */
private String getVehichleRegNumber() throws Exception {
	logger.info("Please type the vehicle registration number and press enter key");
	return inputReaderUtil.readVehicleRegistrationNumber();
}

/**
 * Method to get the next parking spot number available and to create a parking
 * spot for user
 *
 * @return parkingSpot
 */
public ParkingSpot getNextParkingNumberIfAvailable() {
	final int parkingNumber;
	ParkingSpot parkingSpot = null;
	try {
		final ParkingType parkingType = getVehichleType();
		parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
		if (parkingNumber > 0) {
			parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
		} else {
			throw new IllegalArgumentException("Error fetching parking number from DB. Parking slots might be full");
		}
	} catch (final IllegalArgumentException ie) {
		logger.error("Error parsing user input for type of vehicle", ie);
	} catch (final Exception e) {
		logger.error("Error fetching next available parking slot", e);
	}
	return parkingSpot;
}

/**
 * @return parking type
 */
private ParkingType getVehichleType() {
	logger.info("Please select vehicle type from menu");
	logger.info("1 CAR");
	logger.info("2 BIKE");
	final int input = inputReaderUtil.readSelection();
	switch (input) {
		case 1:
			return ParkingType.CAR;
		case 2:
			return ParkingType.BIKE;
		default:
			logger.info("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
	}
}

/**
 * Method to update out time and calculate the fare for a user
 */
public void processExitingVehicle() {
	try {
		final String vehicleRegNumber = getVehichleRegNumber();
		final Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
		final Date outTime = new Date();
		ticket.setOutTime(outTime);
		final boolean recurring = ticketDAO.isAlreadyClient(vehicleRegNumber);
		fareCalculatorService.calculateFare(ticket);
		if (ticketDAO.updateTicket(ticket)) {
			final ParkingSpot parkingSpot = ticket.getParkingSpot();
			parkingSpot.setAvailable(true);
			parkingSpotDAO.updateParking(parkingSpot);
			if (recurring) {
				logger.info("Please pay the parking fare:{}", ticket.getPrice());
				logger.info("Recorded out-time for vehicle number:{} is {}", ticket.getVehicleRegNumber(), outTime);
			} else {
				logger.info("Please pay the parking fare:{}", ticket.getPrice());
				logger.info("Recorded out-time for vehicle number:{} is:{}", ticket.getVehicleRegNumber(), outTime);
			}
		} else {
			logger.info("Unable to update ticket information. Error occurred");
		}
	} catch (final Exception e) {
		logger.error("Unable to process exiting vehicle", e);
	}
}
}