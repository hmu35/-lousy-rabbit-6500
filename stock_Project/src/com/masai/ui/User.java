package com.masai.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.masai.dbUtils.DBUtils;
import com.masai.exception.SomeThingWrongException;

public class User {
	
	static void displayUserMenu() {
		System.out.println("1. View all the stocks.");
		System.out.println("2. Buy and sell stocks.");
		System.out.println("3. View Transaction History.");
		System.out.println("4. Add & withdraw fund to & from wallet.");
		System.out.println("0. for Exit");
	}
	
	public static void isUser(String username,String password) {
		Connection con = null;
		try {
			 con = DBUtils.connectToDatabase();
			
			String insertQuery = "select * from customer where username = ? && password = ?";
			
			PreparedStatement ps = con.prepareStatement(insertQuery);
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(DBUtils.isResultSetEmpty(rs)) {
				System.out.println("Wrong username or password");
			}else {
				System.out.println("Welcome "+ username);
				displayUserMenu();
				System.exit(0);
			}

			
			
		} catch (SQLException e) {
			
			new SomeThingWrongException();
		} finally {
			try {
				DBUtils.closeConnection(con);
			} catch (SQLException e) {
				
				new SomeThingWrongException();
			}
		}
		
		
	}
	
}
