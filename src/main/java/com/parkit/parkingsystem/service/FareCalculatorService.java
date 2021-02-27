package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;

import java.time.Duration;
import java.time.LocalDateTime;


public class FareCalculatorService {
    boolean recurring;
    private double duration;

    public double calculateFare(Ticket ticket, boolean recurring) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();
        //TODO: Some tests are failing here. Need to check if this logic is correct
        
        Duration durationBetweenInTimeAndOutTime = Duration.between(inHour, outHour);
        double durationMin = durationBetweenInTimeAndOutTime.toMinutes();
        double duration = durationMin / 60; // Free for the first half hours
    
        if (duration > 0.5) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        } else {
            ticket.setPrice(0);
        }
        return duration;
        
    }
    
   /* public void calculateFare(Ticket ticket) {
        double disCount =1;
        if (ticketDAO.isAlreadyClient(ticket.getVehicleRegNumber())) {
            disCount = Fare.REDUCTION_OF_RECURRING_USERS;
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * disCount);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * disCount);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }*/
}
