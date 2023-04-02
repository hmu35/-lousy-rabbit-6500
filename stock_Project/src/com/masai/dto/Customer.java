package com.masai.dto;

public class Customer {
		
	int id;
	String username;
	String password;
	boolean status;
	int wallet;
	
	public Customer() {};
	
	public Customer(int id, String username, String password, boolean status, int wallet) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.status = status;
		this.wallet = wallet;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}

	@Override
	public String toString() {
		return "Customeid=" + id + ", username=" + username + ", password=" + password + ", status=" + status
				+ ", wallet=" + wallet +"\n";
	}
	
	
	
	
}
