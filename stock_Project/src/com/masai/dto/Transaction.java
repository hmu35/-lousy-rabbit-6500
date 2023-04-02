package com.masai.dto;

public class Transaction {
	
	int sid;
	int cid;
	int quanity;
	String type;
	
	public Transaction() {};
	
	public Transaction(int sid, int cid, int quanity, String type) {
		this.sid = sid;
		this.cid = cid;
		this.quanity = quanity;
		this.type = type;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "sid=" + sid + ", cid=" + cid + ", quanity=" + quanity + ", type=" + type + "\n";
	}
	
	
	
	
	
	
}
