package com.datamodel.anvizent.common.spring.email;

/**
 * Email configuration
 * 
 * @author Hari Anasuri
 *
 */
final public class EmailConfig {
	
	public final static String DO_NOT_REPLY = "noreply@argha.com";
	public final static String DO_NOT_REPLY_ALIAS = "Argha Architectural - Do Not Reply";
	private String administrator;
	private String from;
	private String fromAlias;
	
	public EmailConfig() {}
	
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromAlias() {
		return fromAlias;
	}
	public void setFromAlias(String fromAlias) {
		this.fromAlias = fromAlias;
	}
}
