package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Class to save, to get, to update and to check a ticket in DB.
 *
 * @author Clévyd
 */
public class TicketDAO {

private static final Logger logger = LogManager.getLogger("TicketDAO");
public DataBaseConfig dataBaseConfig = new DataBaseConfig();

/**
 * Method to save a ticket in DB.
 *
 * @param ticket
 * @return boolean true (ps.execute()) or false it doesn't save the ticket
 */
public boolean saveTicket(final Ticket ticket) {
	Connection con = null;
	PreparedStatement ps = null;
	try {
		con = dataBaseConfig.getConnection();
		ps = con.prepareStatement(DBConstants.SAVE_TICKET);
		ps.setInt(1, ticket.getParkingSpot().getId());
		ps.setString(2, ticket.getVehicleRegNumber());
		ps.setDouble(3, ticket.getPrice());
		
		ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
		ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
		return ps.execute();
	} catch (final Exception ex) {
		logger.error("Error fetching next available slot", ex);
	} finally {
		dataBaseConfig.closeConnection(con);
		dataBaseConfig.closePreparedStatement(ps);
	}
	return false;
}

/**
 * Method to get a ticket from DB.
 *
 * @param vehicleRegNumber
 * @return ticket that is in the database
 */
public Ticket getTicket(final String vehicleRegNumber) {
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Ticket ticket = null;
	try {
		con = dataBaseConfig.getConnection();
		ps = con.prepareStatement(DBConstants.GET_TICKET);
		ps.setString(1, vehicleRegNumber);
		rs = ps.executeQuery();
		if (rs.next()) {
			ticket = new Ticket();
			final ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
			ticket.setParkingSpot(parkingSpot);
			ticket.setId(rs.getInt(2));
			ticket.setVehicleRegNumber(vehicleRegNumber);
			ticket.setPrice(rs.getDouble(3));
			ticket.setInTime(rs.getTimestamp(4));
			ticket.setOutTime(rs.getTimestamp(5));
		}
	} catch (final Exception ex) {
		logger.error("Error fetching next available slot", ex);
	} finally {
		dataBaseConfig.closeResultSet(rs);
		dataBaseConfig.closePreparedStatement(ps);
		dataBaseConfig.closeConnection(con);
	}
	return ticket;
}

/**
 * Method to update a ticket in DB.
 *
 * @param ticket
 * @return boolean true (ps.execute()) or
 * false if "id" doesn't update the ticket in DB
 */
public boolean updateTicket(final Ticket ticket) {
	Connection con = null;
	PreparedStatement ps = null;
	try {
		con = dataBaseConfig.getConnection();
		ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
		ps.setDouble(1, ticket.getPrice());
		ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
		ps.setInt(3, ticket.getId());
		ps.execute();
		return true;
	} catch (final Exception ex) {
		logger.error("Error saving ticket info", ex);
	} finally {
		dataBaseConfig.closeConnection(con);
		dataBaseConfig.closePreparedStatement(ps);
	}
	return false;
}


/**
 * Method for checking that the vehicle is already a customer.
 *
 * @param vehicleRegNumber
 * @return recurring
 */
public boolean isAlreadyClient(final String vehicleRegNumber) {
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean recurring = false;
	int count = 0;
	
	try {
		con = dataBaseConfig.getConnection();
		ps = con.prepareStatement(DBConstants.COUNT_TICKET);
		ps.setString(1, vehicleRegNumber);
		rs = ps.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
			logger.info(count);
		}
		if (count >= 1) {
			recurring = true;
		}
	} catch (final Exception ex) {
		logger.error("Error to verify if it's a recurring user", ex);
	} finally {
		dataBaseConfig.closeResultSet(rs);
		dataBaseConfig.closePreparedStatement(ps);
		dataBaseConfig.closeConnection(con);
	}
	return recurring;
}
}

