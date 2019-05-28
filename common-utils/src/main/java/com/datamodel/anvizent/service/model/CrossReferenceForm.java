/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.util.List;

/**
 * @author rakesh.gajula
 *
 */
public class CrossReferenceForm {
	private Integer ilId;
	private String ilName;
	private String crossReferenceOption;
	private String typeOfMerge;
	private String conditionName;
	private String applicableDate;
	private String ilColumnName;
	private List<String> ilColumnValue;
	private Integer ilValue;
	private String ilXreferenceColumn;
	private List<String> ilMergeColumns;
	private String autoincrementColumnName;
	private String xRefKeyColumnName;
	private String ilXreferenceValue;
	private String newOrExistingXref = "newXref";
	private String existingXrefValue;
	private String existingXrefKey;
	private Integer id;
	private String conditionObject;
	private String xrefExecutionType;
	
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public String getIlName() {
		return ilName;
	}
	public void setIlName(String ilName) {
		this.ilName = ilName;
	}
	public String getCrossReferenceOption() {
		return crossReferenceOption;
	}
	public void setCrossReferenceOption(String crossReferenceOption) {
		this.crossReferenceOption = crossReferenceOption;
	}
	public String getTypeOfMerge() {
		return typeOfMerge;
	}
	public void setTypeOfMerge(String typeOfMerge) {
		this.typeOfMerge = typeOfMerge;
	}
	public String getConditionName() {
		return conditionName;
	}
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
	public String getApplicableDate() {
		return applicableDate;
	}
	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
	}
	public String getIlColumnName() {
		return ilColumnName;
	}
	public void setIlColumnName(String ilColumnName) {
		this.ilColumnName = ilColumnName;
	}
	public List<String> getIlColumnValue() {
		return ilColumnValue;
	}
	public void setIlColumnValue(List<String> ilColumnValue) {
		this.ilColumnValue = ilColumnValue;
	}
	public String getIlXreferenceColumn() {
		return ilXreferenceColumn;
	}
	public void setIlXreferenceColumn(String ilXreferenceColumn) {
		this.ilXreferenceColumn = ilXreferenceColumn;
	}
	public List<String> getIlMergeColumns() {
		return ilMergeColumns;
	}
	public void setIlMergeColumns(List<String> ilMergeColumns) {
		this.ilMergeColumns = ilMergeColumns;
	}
	public String getAutoincrementColumnName() {
		return autoincrementColumnName;
	}
	public void setAutoincrementColumnName(String autoincrementColumnName) {
		this.autoincrementColumnName = autoincrementColumnName;
	}
	public String getxRefKeyColumnName() {
		return xRefKeyColumnName;
	}
	public void setxRefKeyColumnName(String xRefKeyColumnName) {
		this.xRefKeyColumnName = xRefKeyColumnName;
	}
	public String getIlXreferenceValue() {
		return ilXreferenceValue;
	}
	public void setIlXreferenceValue(String ilXreferenceValue) {
		this.ilXreferenceValue = ilXreferenceValue;
	}
	public String getNewOrExistingXref() {
		return newOrExistingXref;
	}
	public void setNewOrExistingXref(String newOrExistingXref) {
		this.newOrExistingXref = newOrExistingXref;
	}
	public String getExistingXrefValue() {
		return existingXrefValue;
	}
	public void setExistingXrefValue(String existingXrefValue) {
		this.existingXrefValue = existingXrefValue;
	}
	public String getExistingXrefKey() {
		return existingXrefKey;
	}
	public void setExistingXrefKey(String existingXrefKey) {
		this.existingXrefKey = existingXrefKey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIlValue() {
		return ilValue;
	}
	public void setIlValue(Integer ilValue) {
		this.ilValue = ilValue;
	}
	public String getConditionObject()
	{
		return conditionObject;
	}
	public void setConditionObject(String conditionObject)
	{
		this.conditionObject = conditionObject;
	}
	public String getXrefExecutionType() {
		return xrefExecutionType;
	}
	public void setXrefExecutionType(String xrefExecutionType) {
		this.xrefExecutionType = xrefExecutionType;
	}
	
	@Override
	public String toString()
	{
		return "CrossReferenceForm [ilId=" + ilId + ", ilName=" + ilName + ", crossReferenceOption=" + crossReferenceOption + ", typeOfMerge=" + typeOfMerge + ", conditionName=" + conditionName + ", applicableDate=" + applicableDate + ", ilColumnName=" + ilColumnName + ", ilColumnValue="
				+ ilColumnValue + ", ilValue=" + ilValue + ", ilXreferenceColumn=" + ilXreferenceColumn + ", ilMergeColumns=" + ilMergeColumns + ", autoincrementColumnName=" + autoincrementColumnName + ", xRefKeyColumnName=" + xRefKeyColumnName + ", ilXreferenceValue=" + ilXreferenceValue
				+ ", newOrExistingXref=" + newOrExistingXref + ", existingXrefValue=" + existingXrefValue + ", existingXrefKey=" + existingXrefKey + ", id=" + id + ", conditionObject=" + conditionObject + ", xrefExecutionType=" + xrefExecutionType + "]";
	}
	
	
	
}
