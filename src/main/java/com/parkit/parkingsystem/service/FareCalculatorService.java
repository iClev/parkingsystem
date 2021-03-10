package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Class to contain the fare's calculator
 *
 * @author Clévyd
 */
public class FareCalculatorService {

/**
 * Method to calculate the fare with duration staying to park.
 *
 * @param ticket
 * @return duration
 */
public double calculateFare(final Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
		throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}
	final double inHour = ticket.getInTime().getTime();
	final double outHour = ticket.getOutTime().getTime();
	final double duration = (outHour - inHour) / (1000 * 60 * 60);
	if (duration > 0.5) {
		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR:
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				break;
			case BIKE:
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				break;
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
		}
	} else {
		ticket.setPrice(0);
	}
	return duration;
}
}
