package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class FareCalculatorServiceTest {
    public static FareCalculatorService fareCalculatorService;
    public static LocalDateTime inTime;
    public static LocalDateTime outTime;
    public static Ticket ticket;
    boolean recurring;
    private static ParkingSpot parkingSpot;
    
@BeforeAll
   public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
        
   }
    
    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        Date inTime = new Date();
        inTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Date outTime = new Date();
        outTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
    }

    @Test
    public void calculateFareCar(){
        ticket.setOutTime(ticket.getInTime().plusHours(1));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, false);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        ticket.setOutTime(ticket.getInTime().plusHours(1));
        parkingSpot.setParkingType(ParkingType.BIKE);
        fareCalculatorService.calculateFare(ticket, recurring);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        ticket.setOutTime(ticket.getInTime().plusHours(1));
        parkingSpot.setParkingType(null);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket,false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        ticket.setOutTime(ticket.getInTime().minusHours(1));
        parkingSpot.setParkingType(ParkingType.BIKE);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,false));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
       //45 minutes parking time should give 3/4th parking fare
        ticket.setOutTime(ticket.getInTime().plusMinutes(45));
        parkingSpot.setParkingType(ParkingType.BIKE);
        fareCalculatorService.calculateFare(ticket,false);
         assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
       //45 minutes parking time should give 3/4th parking fare
        ticket.setOutTime(ticket.getInTime().plusMinutes(45));
        parkingSpot.setParkingType(ParkingType.CAR);
        System.out.println(ticket.getPrice());
        fareCalculatorService.calculateFare(ticket, recurring);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        //24 hours parking time should give 24 * parking fare per hour
        ticket.setOutTime(ticket.getInTime().plusDays(1));
        parkingSpot.setParkingType(ParkingType.CAR);
        fareCalculatorService.calculateFare(ticket,false);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

}
