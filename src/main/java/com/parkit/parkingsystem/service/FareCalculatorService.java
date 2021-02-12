package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;


public class FareCalculatorService {

    private long duration;

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct

        long duration = (outHour - inHour)/1000/60/60; // Free for the first half hours
        if(duration < 0.5) {
            duration = 0;
            ticket.setPrice(0);
            return;
        }
        switch (ticket.getParkingSpot().getParkingType()){
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
    }
    //5% discount for recurring users
    public void calculateFare(Ticket ticket, TicketDAO ticketDAO) {
        double discount =1;
        if (ticketDAO.isAlreadyClient(ticket.getVehicleRegNumber())) {
            discount = Fare.REDUCTION_OF_RECURRING_USERS;
        }
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * discount);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * discount);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
