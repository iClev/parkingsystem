package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Class to open and close connection.
 *
 * @author Clévyd
 *
 **/
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    
    private static final String FILE_PATH = "src/main/ressources/credentials.properties";
    
    private String url;

    private String userName;
    
    private String passWord;


/**
 * Method to open MySQL DB connection.
 *
 * @return Connection to database
 * @throws ClassNotFoundException;
 * @throws SQLException;
 */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (InputStream inputStream = new FileInputStream(FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            url = properties.getProperty("urlProd");
            userName = properties.getProperty("userName");
            passWord = properties.getProperty("passWord");
        } catch (FileNotFoundException fnf) {
            logger.error("File not found. Please verify credentials file access root.", fnf);
        } catch (IOException ioe) {
           logger.error("Error during DB connection. Please check the contents file.", ioe);
        }
        return DriverManager.getConnection(url, userName, passWord);
    }
    
/**
 * Method to close database connection.
 *
 * @param con as Connection instance to be closed
 */
    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

/**
 * Method to close preparedStatement.
 *
 * @param ps an instance of PreparedStatement to be closed
 */
    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

/**
 * Method to close the resultSet.
 *
 * @param rs an instance of ResultSet to be closed
 */
    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
