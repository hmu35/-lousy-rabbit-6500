package com.masai.dto;

public class CustomerStockMap {
	
	int cid;
	int sid;
	int quanity;
	
	public CustomerStockMap() {};
	
	public CustomerStockMap(int cid, int sid, int quanity) {
		this.cid = cid;
		this.sid = sid;
		this.quanity = quanity;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	@Override
	public String toString() {
		return "CustomerStockMap [cid=" + cid + ", sid=" + sid + ", quanity=" + quanity + "]";
	}
	
	
	
	
}
