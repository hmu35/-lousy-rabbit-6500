package com.masai.dto;

public class Stocks {
	int id ;
	String name;
	int quantity;
	int price;
	int totalQuantity;
	
	public Stocks() {};
	
	public Stocks(int id, String name, int quantity, int price, int totalQuantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.totalQuantity = totalQuantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	@Override
	public String toString() {
		return "Stocksid=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", totalQuantity=" + totalQuantity + "\n";
	}
	
	
	
	
	
}
