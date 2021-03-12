package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Clévyd
 */
public class DataBaseTestConfig extends DataBaseConfig {

private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

private static final String FILE_PATH = "src/main/resources/credentials.properties";

/**
 * @return
 * @throws ClassNotFoundException
 * @throws SQLException
 */
@Override
public Connection getConnection() throws ClassNotFoundException, SQLException {
	logger.info("Create DB connection");
	Class.forName("com.mysql.cj.jdbc.Driver");
	try (InputStream inputStream = new FileInputStream(FILE_PATH)) {
		Properties properties = new Properties();
		properties.load(inputStream);
		return DriverManager.getConnection(properties.getProperty("urlTest"), properties.getProperty("userName"),properties.getProperty("passWord"));
	} catch (FileNotFoundException fnf) {
		logger.error("File not found. Please verify credentials file access root.", fnf);
	} catch (IOException ioe) {
		logger.error("Error during DB connection. Please check the contents file.", ioe);
	}
	return null;
}

/**
 * @param con as Connection instance to be closed
 */
@Override
public void closeConnection(Connection con) {
	if (con != null) {
		try {
			con.close();
			logger.info("Closing DB connection");
		} catch (SQLException e) {
			logger.error("Error while closing connection", e);
		}
	}
}

/**
 * @param ps an instance of PreparedStatement to be closed
 */
@Override
public void closePreparedStatement(PreparedStatement ps) {
	if (ps != null) {
		try {
			ps.close();
			logger.info("Closing Prepared Statement");
		} catch (SQLException e) {
			logger.error("Error while closing prepared statement", e);
		}
	}
}

/**
 * @param rs an instance of ResultSet to be closed
 */
@Override
public void closeResultSet(ResultSet rs) {
	if (rs != null) {
		try {
			rs.close();
			logger.info("Closing Result Set");
		} catch (SQLException e) {
			logger.error("Error while closing result set", e);
		}
	}
}
}
