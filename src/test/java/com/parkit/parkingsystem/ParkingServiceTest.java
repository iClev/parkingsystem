package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {
@Mock
private static ParkingService parkingService;
@Mock
private static InputReaderUtil inputReaderUtil;
@Mock
private static ParkingSpotDAO parkingSpotDAO;
@Mock
private static TicketDAO ticketDAO;

@BeforeEach
void setUpPerTest() {
	try {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	} catch (final Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Failed to set up test mock objects");
	}
}

@Test
@DisplayName("Unit test incoming CAR or BIKE")
void processIncomingVehicleTest() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
	
	when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
	
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	parkingService.processIncomingVehicle();
	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
}

@Test
@DisplayName("Unit test exiting CAR or BIKE with discount")
void processExitingVehicleTest() {
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	final Ticket ticket = new Ticket();
	ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");
	when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
	parkingService.processExitingVehicle();
	parkingService.processExitingVehicle();
	verify(parkingSpotDAO, Mockito.times(2)).updateParking(any(ParkingSpot.class));
}
}
