package com.masai.dao;

import java.util.List;

import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;

public interface CustomerDao {
	

	List<Stocks> viewStocks()throws SomeThingWrongException,NoRecordFoundException;
	
	void buyStock(int cust_id,int stock_id ,int buyquantity)throws SomeThingWrongException, NoRecordFoundException;
	
	void sellStock(int cust_id,int stock_id,int sellquantity)throws SomeThingWrongException, NoRecordFoundException;
	
	void viewTransactionHistory(int customer_id)throws SomeThingWrongException,NoRecordFoundException; 
	
	
	
}
