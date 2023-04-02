package com.masai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.masai.dbUtils.DBUtils;
import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;


public class CustomerDaoImpl implements CustomerDao {

	@Override
	public List<Stocks> viewStocks() throws SomeThingWrongException, NoRecordFoundException {
		
		Connection connection = null;
		List<Stocks> list = new ArrayList<>();
		try {
			//connect to database
			connection = DBUtils.connectToDatabase();
			//prepare the query
			String SELECT_QUERY = "SELECT * FROM stocks";
			
			//get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(SELECT_QUERY);
			
			//execute query
			ResultSet resultSet = ps.executeQuery();
			
			//check if result set is empty
			if(DBUtils.isResultSetEmpty(resultSet)) {
				throw new NoRecordFoundException("No Stock Found");
			}
			
			while(resultSet.next()) {
				Stocks stk = new Stocks();
				
				stk.setId(resultSet.getInt(1));
				stk.setName(resultSet.getString(2));
				stk.setQuantity(resultSet.getInt(3));
				stk.setPrice(resultSet.getInt(4));
				stk.setTotalQuantity(500);
				
				list.add(stk);
			}
			
		}catch(SQLException sqlEx) {
			//code to log the error in the file
			throw new SomeThingWrongException();
		}finally {
			try {
				//close the exception
				DBUtils.closeConnection(connection);				
			}catch(SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}
		return list;
	}

	@Override
	public void buyStock(int cust_id,int stock_id,int buyquantity) throws SomeThingWrongException, NoRecordFoundException {
		Connection connection = null;
		try {
			
			//connect to database
			connection = DBUtils.connectToDatabase();
			//prepare the query
			String Stock_SELECT_QUERY = "SELECT * FROM stocks where sid = '"+stock_id+"'";
			
			//get the prepared statement object
			PreparedStatement ps_stk = connection.prepareStatement(Stock_SELECT_QUERY);
			
			//execute query
			ResultSet rsstk = ps_stk.executeQuery();
			
			//check if result set is empty
			if(DBUtils.isResultSetEmpty(rsstk)) {
				throw new NoRecordFoundException("No Stock with given stock id Found");
			}
			
			while(rsstk.next()) {
				
				
				int stk_quant = rsstk.getInt("quantity");
				int stk_price = rsstk.getInt("price_per_stock");
				
				String cust_SELECT_QUERY = "SELECT * FROM customer where cid = '"+cust_id+"'";
				
				PreparedStatement ps_cust = connection.prepareStatement(cust_SELECT_QUERY);
				
				ResultSet rscust = ps_cust.executeQuery();
				
				if(DBUtils.isResultSetEmpty(rscust)) {
					throw new NoRecordFoundException("No Customer with given customer id Found");
				}
//				check if have enough stock quantity that customer want to purchase if has then reduce quantity of stock
				
				if(stk_quant < buyquantity ) {
					System.out.println("Buy Quanity amount must be smaller or eqaual to present stock quantity ");
					return;
				}
				
				
				while(rscust.next()) {
					
//					check if customer has enough amount in wallet and if have then update wallet amount
					int cust_wallet = rscust.getInt("wallet");
					
					if(cust_wallet < stk_price*buyquantity ) {
						System.out.println("Not enough money in wallet to buy stock");
						return;
					}

					int updated_wal_amt = cust_wallet - stk_price*buyquantity;
					
					
					String update_wallet_amount = "UPDATE customer set wallet = '"+updated_wal_amt+"'where cid = '"+cust_id+"'";
					PreparedStatement ps_update_wal = connection.prepareStatement(update_wallet_amount);
					ps_update_wal.executeUpdate();
					
					int updated_stk_quan = stk_quant - buyquantity;
					
					String update_stock_quantity = "UPDATE stocks set quantity = '"+updated_stk_quan+"'where sid = '"+stock_id+"'";
					PreparedStatement ps_update_stkQuant = connection.prepareStatement(update_stock_quantity);
					ps_update_stkQuant.executeUpdate();
					
					
					
					
					
//					customer_stock_mapping table;
//					check if already in table then update quantity else add new field in table
					String custStockMap_SELECT_QUERY = "SELECT * FROM customer_stock_mapping where cid = '"+cust_id+"' && sid = '"+stock_id+"'";
					
					PreparedStatement ps_custStockMap = connection.prepareStatement(custStockMap_SELECT_QUERY);
					
					ResultSet rscustStockMap = ps_custStockMap.executeQuery();
					
					if(DBUtils.isResultSetEmpty(rscustStockMap)) {
						
						String custStockMap_insert_QUERY="insert into customer_stock_mapping values(?,?,?)";
						
						PreparedStatement  psInsertCustStkMap = connection.prepareStatement(custStockMap_insert_QUERY);
						psInsertCustStkMap.setInt(1, cust_id);
						psInsertCustStkMap.setInt(2, stock_id);
						psInsertCustStkMap.setInt(3, buyquantity);
						psInsertCustStkMap.executeUpdate();
						
						
					}
					while(rscustStockMap.next()) {
						int quanityOfstockid = rscustStockMap.getInt("quantity");
						
						int newquanityOfstockid = quanityOfstockid+ buyquantity;
//						String updatequanityOfstockid = "UPDATE customer_stock_mapping set quantity ="+ newquanityOfstockid +  "where cid = "+cust_id+" &&  sid ="+ stock_id;
						String updatequanityOfstockid = "UPDATE customer_stock_mapping set quantity = ? where cid = ? &&  sid =?";
						PreparedStatement  psupdatequanityOfstockid = connection.prepareStatement(updatequanityOfstockid);
						psupdatequanityOfstockid.setInt(1, newquanityOfstockid);
						psupdatequanityOfstockid.setInt(2, cust_id);
						psupdatequanityOfstockid.setInt(3, stock_id);
						
						
						psupdatequanityOfstockid.executeUpdate();

						
						
					}
					
//					*********************************************************************
					
					
//					Adding buy stock info in transaction table
					
					String transaction_add_QUERY = "insert into transaction values(?,?,?,'buy')";
					
					PreparedStatement ps_transaction = connection.prepareStatement(transaction_add_QUERY);
					
					ps_transaction.setInt(1,cust_id);
					ps_transaction.setInt(2,stock_id);
					ps_transaction.setInt(3,buyquantity);
					
					
					if(ps_transaction.executeUpdate()>0) {
						
						System.out.println("Thank you For Purchasing stock.");
						return;
					}else {
						System.out.println("Unable to purchase because of some error");
						return;
					}
					
					
				}
				
				
				
				
				
			}
			
		}catch(SQLException sqlEx) {
			//code to log the error in the file
//			throw new SomeThingWrongException();
		}finally {
			try {
				//close the exception
				DBUtils.closeConnection(connection);				
			}catch(SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}
		
		
	}
	
	
	
	
	

	@Override
	public void sellStock(int cust_id,int stock_id,int sellquantity) throws SomeThingWrongException, NoRecordFoundException {
		
		Connection connection = null;
		try {
			
			//connect to database
			connection = DBUtils.connectToDatabase();
			//prepare the query
			String Stock_SELECT_QUERY = "SELECT * FROM customer_stock_mapping where cid = '"+cust_id+"' && sid = '"+stock_id+"' ";
			
			//get the prepared statement object
			PreparedStatement ps_stk = connection.prepareStatement(Stock_SELECT_QUERY);
			
			//execute query
			ResultSet rsstk = ps_stk.executeQuery();
			
			//check if result set is empty
			if(DBUtils.isResultSetEmpty(rsstk)) {
				throw new NoRecordFoundException("No Stock with given stock id Found");
			}
			
			while(rsstk.next()) {

				int quantityOfstk = rsstk.getInt(3);
				
//				check if customer want to sell stock quantity more than he owns
				
				if(sellquantity > quantityOfstk) {
					System.out.println("Not enough stock to sell");
					return;
				}
//				update stockQuantity in customer_stock_mapping table
				
				int quanityOfstockid = rsstk.getInt("quantity");
				
				int newquanityOfstockid = quanityOfstockid - sellquantity;
//				String updatequanityOfstockid = "UPDATE customer_stock_mapping set quantity ="+ newquanityOfstockid +  "where cid = "+cust_id+" &&  sid ="+ stock_id;
				String updatequanityOfstockid = "UPDATE customer_stock_mapping set quantity = ? where cid = ? &&  sid =?";
				PreparedStatement  psupdatequanityOfstockid = connection.prepareStatement(updatequanityOfstockid);
				psupdatequanityOfstockid.setInt(1, newquanityOfstockid);
				psupdatequanityOfstockid.setInt(2, cust_id);
				psupdatequanityOfstockid.setInt(3, stock_id);
				
				
				psupdatequanityOfstockid.executeUpdate();
				
				
				
				
				
				
				
//				calculate the price of stocks customer own using customer_stock_mapping inner join stocks
				
				String getPriceOfStk = "select price_per_stock from customer_stock_mapping inner join stocks on customer_stock_mapping.sid = stocks.sid  ";
				
				PreparedStatement ps_getPriceOfStk  = connection.prepareStatement(getPriceOfStk);
				
				ResultSet rsgetPriceOfStk = ps_getPriceOfStk.executeQuery();
				
				while(rsgetPriceOfStk.next()) {
					int stk_price = rsgetPriceOfStk.getInt(1);
//					System.out.println(stk_price);
					
//					now update wallet amount of customer ie add stock price*sell quanity
//					first get the customer with given id
					String cust_SELECT_QUERY = "SELECT * FROM customer where cid = '"+cust_id+"'";
					
					PreparedStatement ps_cust = connection.prepareStatement(cust_SELECT_QUERY);
					
					ResultSet rscust = ps_cust.executeQuery();
					
					if(DBUtils.isResultSetEmpty(rscust)) {
						throw new NoRecordFoundException("No Customer with given customer id Found");
					}
//					check if have enough stock quantity that customer want to purchase if has then reduce quantity of stock
					
	
					
					
					while(rscust.next()) {
						int walletamount = rscust.getInt(5);
						
						int updated_walletamount = walletamount + stk_price*sellquantity;
						
						String addMoneyInWallet = "Update customer set wallet = ? where cid = ? ";
						
						
						
						PreparedStatement ps_addMoneyInWallet = connection.prepareStatement(addMoneyInWallet);
						
						ps_addMoneyInWallet.setInt(1, updated_walletamount);
						ps_addMoneyInWallet.setInt(2,cust_id);
						
						
						ps_addMoneyInWallet.executeUpdate();
						
						
//						also add back stock in stocks quantity in stocks table;
						
						String addStockInStockstable =  "SELECT * FROM stocks where sid = ?";
						PreparedStatement ps_addStockInStockstable = connection.prepareStatement(addStockInStockstable);
						ps_addStockInStockstable.setInt(1, stock_id);
						ResultSet rsaddStockInStockstable = ps_addStockInStockstable.executeQuery();
						
						while(rsaddStockInStockstable.next()) {
							int quanity = rsaddStockInStockstable.getInt(3);
							int updated_quant = quanity+ sellquantity;
							
							String updateStkQuantityInStocksTable = "Update stocks set quantity =? where sid = ?";
							
							PreparedStatement ps_updateStkQuantityInStocksTable = connection.prepareStatement(updateStkQuantityInStocksTable);
							
							ps_updateStkQuantityInStocksTable.setInt(1, updated_quant);
							ps_updateStkQuantityInStocksTable.setInt(2, stock_id);
							
							ps_updateStkQuantityInStocksTable.executeUpdate();
							
//							add sell stock in transaction table also;
							
							String transaction_add_QUERY = "insert into transaction values(?,?,?,'sell')";
							
							PreparedStatement ps_transaction = connection.prepareStatement(transaction_add_QUERY);
							
							ps_transaction.setInt(1,cust_id);
							ps_transaction.setInt(2,stock_id);
							ps_transaction.setInt(3,sellquantity);
							
							
							if(ps_transaction.executeUpdate()>0) {
								
								System.out.println("Thank you For Selling stock,Money is added in your Wallet Successfully.");
								return;
							}else {
								System.out.println("Unable to purchase because of some error");
								return;
							}
							
							
							
						}
						
						
						
					} 
					
			       
					
					
					
					
				}
					
				}
				
				
				
				
				
			
		}catch(SQLException sqlEx) {
			//code to log the error in the file
//			throw new SomeThingWrongException();
		}finally {
			try {
				//close the exception
				DBUtils.closeConnection(connection);				
			}catch(SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}
		
		
		
	}

	
	
	
	
	
	
	
	@Override
	public void viewTransactionHistory(int customer_id) throws SomeThingWrongException, NoRecordFoundException {
		Connection connection = null;
		try {
			//connect to database
			connection =DBUtils.connectToDatabase();
			//prepare the query
			String SELECT_QUERY = "select stock_name,type,transaction.quantity from transaction inner join stocks on transaction.sid = stocks.sid where cid = '"+customer_id+"' ";;
			
			//get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(SELECT_QUERY);
			
			//execute query
			ResultSet resultSet = ps.executeQuery();
			
			//check if result set is empty
			if(DBUtils.isResultSetEmpty(resultSet)) {
				throw new NoRecordFoundException("No Transaction Record of Customer");
			}
			
			while(resultSet.next()) {
				System.out.println("Stock Name = "+ resultSet.getString(1)+" Type = "+ resultSet.getString(2)+ " Quanity = "+ resultSet.getInt(3));
			}
			
		}catch(SQLException sqlEx) {
			//code to log the error in the file
			throw new SomeThingWrongException();
		}finally {
			try {
				//close the exception
				DBUtils.closeConnection(connection);				
			}catch(SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}
		
		
		
	}

}
