package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Clévyd
 * @version 3.0
 * @see Fare
 * @see ParkingSpotDAO
 * @see TicketDAO
 * @see DataBaseTestConfig
 * @see DataBasePrepareService
 * @see ParkingSpot
 * @see Ticket
 * @see ParkingService
 * @see InputReaderUtil
 */
@ExtendWith(MockitoExtension.class)
class ParkingDataBaseIT {

private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
private static final String vehicleRegNumber = "ABCDEF";
private static ParkingSpotDAO parkingSpotDAO;
private static TicketDAO ticketDAO;
private static DataBasePrepareService dataBasePrepareService;
@Mock
private static InputReaderUtil inputReaderUtil;

@BeforeAll
private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	dataBasePrepareService = new DataBasePrepareService();
}

@AfterAll
private static void tearDown() {
}

@BeforeEach
void setUpPerTest() throws Exception {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService.clearDataBaseEntries();
}

/**
 * Integration test incoming car
 *
 * @throws Exception
 */
@Test
@DisplayName("Integration test incoming car")
void testParkingACar() throws Exception {
	
	/**
	 * GIVEN : call ParkingService
	 */
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	
	/**
	 * WHEN : call parkingService.processIncomingVehicle
	 */
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	parkingService.processIncomingVehicle();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	/**
	 * THEN : check that ticket is actually saved in DB and parking table is update in DB
	 */
	Ticket ticket = new Ticket();
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber(vehicleRegNumber);
	ticket.setPrice(0);
	ticket.setInTime(new Date());
	assertNotNull(ticketDAO.getTicket("ABCDEF"));
	assertNotNull(ticket.getParkingSpot());
	assertNotNull(ticket.getVehicleRegNumber());
	assertEquals(0, ticket.getPrice());
	assertNotNull(ticket.getInTime());
}

/**
 * Integration test exiting car
 *
 * @throws Exception
 */
@Test
@DisplayName("Integration test exiting car")
void testParkingLotExit() throws Exception {
	
	/**
	 * GIVEN : call ParkingService
	 */
	testParkingACar();
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	
	/**
	 * WHEN : call parkingService.processExitingVehicle
	 */
	Thread.sleep(500);
	parkingService.processIncomingVehicle();
	
	/**
	 * THEN : check fare generate and out time populated
	 */
	ticketDAO.getTicket("ABCDEF");
	ticketDAO.getTicket("ABCDEF").setPrice(Fare.CAR_RATE_PER_HOUR);
	
	boolean testRecurrent = ticketDAO.isAlreadyClient("ABCDEF");
	assertTrue(testRecurrent);
}
}
