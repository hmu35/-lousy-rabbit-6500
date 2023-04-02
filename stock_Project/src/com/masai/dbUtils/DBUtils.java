package com.masai.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException ex) {
			System.err.println("Fatal Error! Unable to start application");
			System.exit(1);
		}
		
	}
	
	public static Connection connectToDatabase() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/stock_project","root","root");
	}
	
	public static void closeConnection(Connection conn) throws SQLException{
		if(conn != null)
			conn.close();
	}
	
	/**
	 * checks if the resultset is empty or not
	 * @param rs - The resultset to be checked for empty
	 * @return - true of resultset is empty, false otherwise
	 * @throws SQLException - if anything went wrong during this operation
	 */
	public static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
		return (!rs.isBeforeFirst() && rs.getRow() == 0)?true:false;
	}
}
