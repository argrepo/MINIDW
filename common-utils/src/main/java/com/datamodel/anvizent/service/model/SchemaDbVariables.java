package com.datamodel.anvizent.service.model;

public class SchemaDbVariables {
	
	private String dbVariable;
	private String dbValue;
	private String schemaVariable;
	private String schemaValue ;
	
	public String getDbVariable() {
		return dbVariable;
	}
	public void setDbVariable(String dbVariable) {
		this.dbVariable = dbVariable;
	}
	public String getDbValue() {
		return dbValue;
	}
	public void setDbValue(String dbValue) {
		this.dbValue = dbValue;
	}
	public String getSchemaVariable() {
		return schemaVariable;
	}
	public void setSchemaVariable(String schemaVariable) {
		this.schemaVariable = schemaVariable;
	}
	public String getSchemaValue() {
		return schemaValue;
	}
	public void setSchemaValue(String schemaValue) {
		this.schemaValue = schemaValue;
	}
	
	@Override
	public String toString() {
		return "SchemaDbVariables [dbVariable=" + dbVariable + ", dbValue=" + dbValue + ", schemaVariable="
				+ schemaVariable + ", schemaValue=" + schemaValue + "]";
	}
	
	
	

	
}
