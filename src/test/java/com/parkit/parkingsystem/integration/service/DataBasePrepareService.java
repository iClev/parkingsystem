package com.parkit.parkingsystem.integration.service;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * Class DataBase Prepare Service.
 */
public class DataBasePrepareService {

/**
 * Data Base Test Config.
 */
private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

/**
 * Clear DataBase Entries.
 */
@SuppressWarnings("checkstyle:LineLength")
public void clearDataBaseEntries() {
	Connection connection = null;
	try {
		connection = dataBaseTestConfig.getConnection();
		List<String> asList = Arrays.asList("update parking set available = true", "truncate table ticket");
		for (String s : asList) {
			connection.prepareStatement(s).execute();
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		dataBaseTestConfig.closeConnection(connection);
	}
}


}
