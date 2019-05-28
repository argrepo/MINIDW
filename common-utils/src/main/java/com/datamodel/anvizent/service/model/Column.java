/**
 * 
 */
package com.datamodel.anvizent.service.model;

/**
 * @author rakesh.gajula
 *
 */
public class Column {
	
	String schemaName;
	String tableName;
	String columnName;
	String dataType;
	String columnSize;
	Boolean isPrimaryKey;
	Boolean isNotNull;
	Boolean isUnique=false;
	Boolean isAutoIncrement;
	String defaultValue;
	Integer decimalPoints;
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Boolean getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(Boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public Boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}
	public Boolean getIsAutoIncrement() {
		return isAutoIncrement;
	}
	public void setIsAutoIncrement(Boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getDecimalPoints() {
		return decimalPoints;
	}
	public void setDecimalPoints(Integer decimalPoints) {
		this.decimalPoints = decimalPoints;
	}
	@Override
	public String toString() {
		return "Column [schemaName=" + schemaName + ", tableName=" + tableName + ", columnName=" + columnName
				+ ", dataType=" + dataType + ", columnSize=" + columnSize + ", isPrimaryKey=" + isPrimaryKey
				+ ", isNotNull=" + isNotNull + ", isUnique=" + isUnique + ", isAutoIncrement=" + isAutoIncrement
				+ ", defaultValue=" + defaultValue + ", decimalPoints=" + decimalPoints + "]";
	}
	

}
