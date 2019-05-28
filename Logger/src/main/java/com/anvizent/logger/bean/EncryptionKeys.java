package com.anvizent.logger.bean;

public class EncryptionKeys {
	private String primaryKey;
	private String iv;

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getIV() {
		return iv;
	}

	public void setIV(String iv) {
		this.iv = iv;
	}

	public EncryptionKeys(String primaryKey, String iv) {
		this.primaryKey = primaryKey;
		this.iv = iv;
	}
}