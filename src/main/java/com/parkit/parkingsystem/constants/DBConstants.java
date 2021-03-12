package com.parkit.parkingsystem.constants;

/**
 * Class contains the different SQL queries.
 * This allows the exchange between the application and the different tables.
 *
 * @author Clévyd
 */
public final class DBConstants {
/**
 * SQL query that is used to search for available parking spots.
 */
public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
/**
 * SQL query that is used to update parking spot availability.
 */
public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
/**
 * SQL query used to save tickets into database.
 */
public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
/**
 * SQL query used to update ticket with price and outTime.
 */
public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
/**
 * SQL query used to retrieve a ticket from database.
 */
public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
/**
 * Compare incoming vehicle registration number.
 * to the vehicles that are using the parking
 */
public static final String COUNT_TICKET = "SELECT count(*) FROM test.ticket WHERE VEHICLE_REG_NUMBER=?";

private DBConstants() {
}
}
