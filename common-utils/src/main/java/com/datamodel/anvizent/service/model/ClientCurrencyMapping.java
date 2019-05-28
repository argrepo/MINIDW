package com.datamodel.anvizent.service.model;

public class ClientCurrencyMapping {
	private int id;
	private String clientId;
	private String currencyName;
	private String currencyType;
	private Modification modification;
	private String pageMode;
	private Boolean isActive;
	private String basecurrencyCode;
	private String accountingCurrencyCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getBasecurrencyCode() {
		return basecurrencyCode;
	}
	public void setBasecurrencyCode(String basecurrencyCode) {
		this.basecurrencyCode = basecurrencyCode;
	}
	public String getAccountingCurrencyCode() {
		return accountingCurrencyCode;
	}
	public void setAccountingCurrencyCode(String accountingCurrencyCode) {
		this.accountingCurrencyCode = accountingCurrencyCode;
	}
	@Override
	public String toString() {
		return "ClientCurrencyMapping [id=" + id + ", clientId=" + clientId + ", currencyType=" + currencyType
				+ ", currencyName=" + currencyName + ", modification=" + modification + ", pageMode=" + pageMode
				+ ", isActive=" + isActive + ", basecurrencyCode=" + basecurrencyCode + ", accountingCurrencyCode="
				+ accountingCurrencyCode + "]";
	}
	
	
	
	
	
}
