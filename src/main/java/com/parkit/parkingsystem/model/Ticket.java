package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * Class to contain ticket model and his getter/setter
 *
 * @author Clévyd
 */
public class Ticket {
private int id;
private ParkingSpot parkingSpot;
private String vehicleRegNumber;
private double price;
private Date inTime;
private Date outTime;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

/**
 * @return parkingSpot
 */
public ParkingSpot getParkingSpot() {
	return parkingSpot;
}

/**
 * @param parkingSpot;
 */
public void setParkingSpot(ParkingSpot parkingSpot) {
	this.parkingSpot = parkingSpot;
}

/**
 * @return vehicleRegNumber
 */
public String getVehicleRegNumber() {
	return vehicleRegNumber;
}

/**
 * @param vehicleRegNumber;
 */
public void setVehicleRegNumber(String vehicleRegNumber) {
	this.vehicleRegNumber = vehicleRegNumber;
}

/**
 * @return price
 */
public double getPrice() {
	return price;
}

/**
 * @param price;
 */
public void setPrice(double price) {
	this.price = price;
}

/**
 * @return inTime
 */
public Date getInTime() {
	return inTime = inTime == null ? null : (Date) inTime.clone();
}

/**
 * @param inTime;
 */
public void setInTime(Date inTime) {
	this.inTime = inTime == null ? null : (Date) inTime.clone();
}

/**
 * @return outTime
 */
public Date getOutTime() {
	return outTime = outTime == null ? null : (Date) outTime.clone();
}

/**
 * @param outTime;
 */
public void setOutTime(Date outTime) {
	this.outTime = outTime == null ? null : (Date) outTime.clone();
}
}