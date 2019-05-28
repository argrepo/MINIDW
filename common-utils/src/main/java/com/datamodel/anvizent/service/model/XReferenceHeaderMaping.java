package com.datamodel.anvizent.service.model;


/**
 * 
 * @author rajesh.anthari
 *
 */

public class XReferenceHeaderMaping {
	int headerMappingId,xReferenceId, userId, packageId, ilMappingId;
	String ilColumnName, sourceColumnName, defaultValue, dataType;
	
	public int getHeaderMappingId() {
		return headerMappingId;
	}
	public void setHeaderMappingId(int headerMappingId) {
		this.headerMappingId = headerMappingId;
	}
	public int getxReferenceId() {
		return xReferenceId;
	}
	public void setxReferenceId(int xReferenceId) {
		this.xReferenceId = xReferenceId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public int getIlMappingId() {
		return ilMappingId;
	}
	public void setIlMappingId(int ilMappingId) {
		this.ilMappingId = ilMappingId;
	}
	public String getIlColumnName() {
		return ilColumnName;
	}
	public void setIlColumnName(String ilColumnName) {
		this.ilColumnName = ilColumnName;
	}
	public String getSourceColumnName() {
		return sourceColumnName;
	}
	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	

	public boolean isSourceColumnEmpty() {
		return ( sourceColumnName == null && defaultValue == null );
	}
	@Override
	public String toString() {
		return "XReferenceHeaderMaping [headerMappingId=" + headerMappingId + ",dataType=" + dataType + ", xReferenceId=" + xReferenceId
				+ ", userId=" + userId + ", packageId=" + packageId + ", ilMappingId=" + ilMappingId + ", ilColumnName="
				+ ilColumnName + ", sourceColumnName=" + sourceColumnName + ", defaultValue=" + defaultValue + "]";
	}
	
}
