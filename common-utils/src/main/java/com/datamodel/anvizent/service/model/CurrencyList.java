package com.datamodel.anvizent.service.model;

public class CurrencyList {
	private int id;
	private String currencyCode;
	private String currencyName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	@Override
	public String toString() {
		return "CurrencyList [id=" + id + ", currencyCode=" + currencyCode + ", currencyName=" + currencyName + "]";
	}
	
		
}
