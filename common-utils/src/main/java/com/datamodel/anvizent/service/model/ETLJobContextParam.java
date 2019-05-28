/**
 * 
 */
package com.datamodel.anvizent.service.model;

/**
 * @author rakesh.gajula
 *
 */
public class ETLJobContextParam {

	private String hostKey;
	private String portKey;
	private String dBNameKey;
	private String userNameKey;
	private String passwordkey;
	private int paramId;
	private String paramName;
	private String paramValue;
	private Boolean isMapped;
	
	public ETLJobContextParam() {
	 
	}
	
	public ETLJobContextParam(int paramId, String paramValue) {
		super();
		this.paramId = paramId;
		this.paramValue = paramValue;
	}
	public String getHostKey() {
		return hostKey;
	}
	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	public String getPortKey() {
		return portKey;
	}
	public void setPortKey(String portKey) {
		this.portKey = portKey;
	}
	public String getdBNameKey() {
		return dBNameKey;
	}
	public void setdBNameKey(String dBNameKey) {
		this.dBNameKey = dBNameKey;
	}
	public String getUserNameKey() {
		return userNameKey;
	}
	public void setUserNameKey(String userNameKey) {
		this.userNameKey = userNameKey;
	}
	public String getPasswordkey() {
		return passwordkey;
	}
	public void setPasswordkey(String passwordkey) {
		this.passwordkey = passwordkey;
	}
	public int getParamId() {
		return paramId;
	}
	public void setParamId(int paramId) {
		this.paramId = paramId;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public Boolean getIsMapped() {
		return isMapped;
	}
	public void setIsMapped(Boolean isMapped) {
		this.isMapped = isMapped;
	}
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	@Override
	public String toString() {
		return "ETLJobContextParam [hostKey=" + hostKey + ", portKey=" + portKey + ", dBNameKey=" + dBNameKey
				+ ", userNameKey=" + userNameKey + ", passwordkey=" + passwordkey + ", paramId=" + paramId
				+ ", paramName=" + paramName + ", paramValue=" + paramValue + ", isMapped=" + isMapped + "]";
	}
	
}
