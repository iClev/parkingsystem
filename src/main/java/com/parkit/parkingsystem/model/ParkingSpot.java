package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Class to contain parking spot model and his getter/setter
 *
 * @author Clévyd
 */
public class ParkingSpot {
private int number;
private ParkingType parkingType;
private boolean isAvailable;

/**
 * @param number;
 * @param parkingType;
 * @param isAvailable;
 */
public ParkingSpot(final int number, final ParkingType parkingType, final boolean isAvailable) {
	this.number = number;
	this.parkingType = parkingType;
	this.isAvailable = isAvailable;
}

/**
 * @return number
 */
public int getId() {
	return number;
}

public void setId(final int number) {
	this.number = number;
}

/**
 * @return parkingType
 */
public ParkingType getParkingType() {
	return parkingType;
}

/**
 * @param parkingType;
 */
public void setParkingType(final ParkingType parkingType) {
	this.parkingType = parkingType;
}

/**
 * @return isAvailable
 */
public boolean isAvailable() {
	return isAvailable;
}

/**
 * @param available;
 */
public void setAvailable(final boolean available) {
	isAvailable = available;
}

/**
 * @param o;
 * @return number
 */
@Override
public boolean equals(final Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;
	final ParkingSpot that = (ParkingSpot) o;
	return number == that.number;
}

/**
 * @return number
 */
@Override
public int hashCode() {
	return number;
}
}
