package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static ParkingSpot parkingSpot;
    private static ParkingService parkingService;
    private static DataBasePrepareService dataBasePrepareService;
    private static String vehicleRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
        
    }

    @BeforeEach
    void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
       
        when(inputReaderUtil.readSelection()).thenReturn(1);
        
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @AfterAll
    private static void tearDown(){
    }

    @Test
    void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime( new Date());
        
        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        assertNotNull(ticket.getParkingSpot());
        assertNotNull(ticket.getVehicleRegNumber());
        assertEquals(0,ticket.getPrice());
        assertNotNull(ticket.getInTime());
    }

    @Test
    void testParkingLotExit() throws Exception{
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        parkingService.processExitingVehicle();
        
        boolean testRecurrent = ticketDAO.isAlreadyClient("ABCDEF");
        assertTrue(testRecurrent);
    }

}
