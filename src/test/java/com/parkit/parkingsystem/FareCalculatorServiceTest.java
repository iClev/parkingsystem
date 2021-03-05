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

import java.util.Date;

 class FareCalculatorServiceTest {
    public static FareCalculatorService fareCalculatorService;
    public static Ticket ticket;
    boolean recurring;
    public static ParkingSpot parkingSpot;
    
@BeforeAll
   public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
   }
    
    @BeforeEach
    public void setUpPerTest() {
        ticket = new Ticket();
        ticket.setInTime(new Date());
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(ticket.getVehicleRegNumber());
    }

    @Test
     void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60*60*1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.CAR_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.BIKE_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        parkingSpot.setParkingType(null);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    
    @Test
    void calculateFareCarWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    @Test
    void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        parkingSpot.setParkingType(ParkingType.BIKE);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    
     @Test
     void calculateFareCarWithLessThanOneHourParkingTime(){
         //45 minutes parking time should give 3/4th parking fare
         Date inTime = new Date();
         inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
         Date outTime = new Date();
         ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    
         ticket.setInTime(inTime);
         ticket.setOutTime(outTime);
         ticket.setParkingSpot(parkingSpot);
         parkingSpot.setParkingType(ParkingType.CAR);
         fareCalculatorService.calculateFare(ticket);
         assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
     }
     
    @Test
    void calculateFareBikeWithLessThanOneHourParkingTime() {
        //45 minutes parking time should give 3/4th parking fare
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        parkingSpot.setParkingType(ParkingType.BIKE);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
        
    }

    @Test
    void calculateFareCarWithMoreThanADayParkingTime(){
        //24 hours parking time should give 24 * parking fare per hour
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        parkingSpot.setParkingType(ParkingType.CAR);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    @Test
    void calculateFareBikeWithMoreThanADayParkingTime() {
        //24 hours parking time should give 24 * parking fare per hour
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        parkingSpot.setParkingType(ParkingType.BIKE);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }
    
    @Test
    void calculateFareCareWithFreeParkingFirstThirtyMinute(){
        // 30 min should give free fare
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

     @Test
     void calculateFareBikeWithFreeParkingFirstThirtyMinute() {
         // 30 min should give free fare
         Date inTime = new Date();
         inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
         Date outTime = new Date();
    
         ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
         ticket.setInTime(inTime);
         ticket.setOutTime(outTime);
         ticket.setParkingSpot(parkingSpot);
         fareCalculatorService.calculateFare(ticket);
         parkingSpot.setParkingType(ParkingType.BIKE);
         assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
     }
}
