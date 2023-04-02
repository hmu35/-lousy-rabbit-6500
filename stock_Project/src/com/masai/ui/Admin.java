package com.masai.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.masai.dao.BrokerDaoImpl;
import com.masai.dbUtils.DBUtils;
import com.masai.dto.Customer;
import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;

public class Admin {
	
	static void displayAdminMenu() {
		System.out.println("1. Register new customer.");
		System.out.println("2. View all the customers.");
		System.out.println("3. Add new stocks.");
		System.out.println("4. View all the stocks.");
		System.out.println("5. View consolidated report of a stock.");
		System.out.println("6. Delete customer");
		System.out.println("7. Delete stock");
		System.out.println("0. for Exit");
	}
	static void adminMenu(Scanner sc,BrokerDaoImpl bdi) throws SomeThingWrongException, NoRecordFoundException {
		int choice = 0;
		do {
			displayAdminMenu();
			System.out.print("Enter selection ");
			choice = sc.nextInt();
			switch(choice) {
				case 0:
					System.out.println("Bye Bye admin");
					break;
				case 1:

					Customer cust = new Customer();
					System.out.println("Enter customer id");
					cust.setId(sc.nextInt());
					System.out.println("Enter username of customer");
					cust.setUsername(sc.next());
					System.out.println("Enter customer password");
					cust.setPassword(sc.next());
					cust.setStatus(true);
					cust.setWallet(500);
					
					bdi.registerCustomer(cust);
					break;
				case 2:
					System.out.println(bdi.viewCustomers());
					break;
				case 3:					
					Stocks stk = new Stocks();
					
					System.out.println("Enter stock id");
					
					stk.setId(sc.nextInt());
					System.out.println("Enter stock name");
					stk.setName(sc.next());
					stk.setQuantity(500);
					System.out.println("Enter per stock amount");
					stk.setPrice(sc.nextInt());
					stk.setTotalQuantity(500);
					bdi.addStock(stk);
					break;
				case 4:
				
					break;
				case 5:
					System.out.println("Enter stock id to view its report");
					bdi.StockReport(sc.nextInt());
					break;
				case 6:
					
					break;
				case 7:
					
					break;
				default:
					System.out.println("Invalid Selection, try again");
			}
		}while(choice != 0);
	}
	
	
	public static void isAdmin(String username,String password,Scanner sc, BrokerDaoImpl bdi) throws SomeThingWrongException, NoRecordFoundException {
		Connection con = null;
		try {
			 con = DBUtils.connectToDatabase();
			
			String insertQuery = "select * from broker where b_username = ? && b_password = ?";
			
			PreparedStatement ps = con.prepareStatement(insertQuery);
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(DBUtils.isResultSetEmpty(rs)) {
				System.out.println("Wrong username or password");
			}else {
				System.out.println("Welcome "+ username);
				adminMenu( sc,bdi);
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
