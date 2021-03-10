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

public class DataBaseTestConfig extends DataBaseConfig {

private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

private static final String FILE_PATH = "src/main/resources/credentials.properties";

private String url;

private String userName;

private String passWord;

public Connection getConnection() throws ClassNotFoundException, SQLException {
	logger.info("Create DB connection");
	Class.forName("com.mysql.cj.jdbc.Driver");
	try (final InputStream inputStream = new FileInputStream(FILE_PATH)) {
		final Properties properties = new Properties();
		properties.load(inputStream);
		url = properties.getProperty("urlTest");
		userName = properties.getProperty("userName");
		passWord = properties.getProperty("passWord");
	} catch (final FileNotFoundException fnf) {
		logger.error("File not found. Please verify credentials file access root.", fnf);
	} catch (final IOException ioe) {
		logger.error("Error during DB connection. Please check the contents file.", ioe);
	}
	return DriverManager.getConnection(url, userName, passWord);
}

public void closeConnection(final Connection con) {
	if (con != null) {
		try {
			con.close();
			logger.info("Closing DB connection");
		} catch (final SQLException e) {
			logger.error("Error while closing connection", e);
		}
	}
}

public void closePreparedStatement(final PreparedStatement ps) {
	if (ps != null) {
		try {
			ps.close();
			logger.info("Closing Prepared Statement");
		} catch (final SQLException e) {
			logger.error("Error while closing prepared statement", e);
		}
	}
}

public void closeResultSet(final ResultSet rs) {
	if (rs != null) {
		try {
			rs.close();
			logger.info("Closing Result Set");
		} catch (final SQLException e) {
			logger.error("Error while closing result set", e);
		}
	}
}
}
