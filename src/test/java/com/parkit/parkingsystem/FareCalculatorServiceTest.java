package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FareCalculatorServiceTest {
private static FareCalculatorService fareCalculatorService;
public Ticket ticket;

@BeforeAll
private static void setUp() {
	fareCalculatorService = new FareCalculatorService();
}

@BeforeEach
private void setUpPerTest() {
	ticket = new Ticket();
}

@Test
@DisplayName("Calculate fare for a CAR")
void calculateFareCar() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for a BIKE")
void calculateFareBike() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for UNKNOW type")
void calculateFareUnkownType() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	parkingSpot.setParkingType(null);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
}

@Test
@DisplayName("Calculate fare for a CAR in the future")
void calculateFareCarWithFutureInTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
}

@Test
@DisplayName("Calculate fare for a BIKE in the future")
void calculateFareBikeWithFutureInTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	parkingSpot.setParkingType(ParkingType.BIKE);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
}

@Test
@DisplayName("Calculate fare for a CAR less than 1 hour")
void calculateFareCarWithLessThanOneHourParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	parkingSpot.setParkingType(ParkingType.CAR);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for a BIKE less than 1 hour")
void calculateFareBikeWithLessThanOneHourParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	parkingSpot.setParkingType(ParkingType.BIKE);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	
}

@Test
@DisplayName("Calculate fare for a CAR more than 1 day")
void calculateFareCarWithMoreThanADayParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	parkingSpot.setParkingType(ParkingType.CAR);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for a BIKE more than 1 day")
void calculateFareBikeWithMoreThanADayParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	parkingSpot.setParkingType(ParkingType.BIKE);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for free less 30 minutes for CAR")
void calculateFareCareWithFreeParkingFirstThirtyMinute() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
}

@Test
@DisplayName("Calculate fare for free less 30 minutes for BIKE")
void calculateFareBikeWithFreeParkingFirstThirtyMinute() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
	final Date outTime = new Date();
	
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	parkingSpot.setParkingType(ParkingType.BIKE);
	assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
}
}
