/**
 * 
 */
package com.datamodel.anvizent.service.model;

/**
 * @author rajesh.anthari
 *
 */
public class CrossReferenceLogs {

	private Integer ilId;
	private String crossReferenceOption;
	private String typeOfMerge;
	private String conditionName;
	private String manualMergeColumnName;
	private String manualMergeColumnValues;
	private String typeOfXref;
	private String existingSelectedXrefValue;
	private String selectedXrefKeyValue;
	private String bulkMergeReferenceFields;
	private String bulkMergeXreferenceFields;
	private int sourceFileInfoId;
	private String autoMergeColumns;
	private String applicableDate;
	private String stats;
	private String startDate;
	private String endDate;
	private Modification modification;
	private int id;
	private String dimension;
	private String conditionObject;
    private boolean mergeStatus;
    private boolean active;
    private long xrefKey;
    private String xrefExecutionType;
	
	/* default constructor */
	public CrossReferenceLogs() {
	}
	
	
	/* for split */
	public CrossReferenceLogs(Integer ilId, String crossReferenceOption, String stats, String startDate, String endDate, Modification modification) {
		super();
		this.ilId = ilId;
		this.crossReferenceOption = crossReferenceOption;
		this.stats = stats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.modification = modification;
	}


	/*  for auto merge*/
	public CrossReferenceLogs(Integer id, Integer ilId, String crossReferenceOption, String typeOfMerge, String conditionName,
			String autoMergeColumns, String applicableDate, String stats,String startDate, String endDate,Modification modification, String conditionObject, String xrefExecutionType) {
		super();
		this.id = id;
		this.ilId = ilId;
		this.crossReferenceOption = crossReferenceOption;
		this.typeOfMerge = typeOfMerge;
		this.conditionName = conditionName;
		this.autoMergeColumns = autoMergeColumns;
		this.applicableDate = applicableDate;
		this.stats = stats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.modification = modification;
		this.conditionObject = conditionObject;
		this.xrefExecutionType = xrefExecutionType;
	}




	/* fro bulk merge*/
	public CrossReferenceLogs(Integer ilId, String crossReferenceOption, String typeOfMerge, String conditionName,
			String bulkMergeReferenceFields, String bulkMergeXreferenceFields, int sourceFileInfoId,
			String applicableDate, String stats, String startDate, String endDate, Modification modification) {
		super();
		this.ilId = ilId;
		this.crossReferenceOption = crossReferenceOption;
		this.typeOfMerge = typeOfMerge;
		this.conditionName = conditionName;
		this.bulkMergeReferenceFields = bulkMergeReferenceFields;
		this.bulkMergeXreferenceFields = bulkMergeXreferenceFields;
		this.sourceFileInfoId = sourceFileInfoId;
		this.applicableDate = applicableDate;
		this.stats = stats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.modification = modification;
	}




	/* for manual merge */
	public CrossReferenceLogs(Integer id, Integer ilId, String crossReferenceOption, String typeOfMerge, String conditionName,
			String manualMergeColumnName, String manualMergeColumnValues, String typeOfXref,
			String existingSelectedXrefValue, String selectedXrefKeyValue, String applicableDate, String stats,
			String startDate, String endDate, Modification modification, Long xrefKey) {
		this.id = id;
		this.ilId = ilId;
		this.crossReferenceOption = crossReferenceOption;
		this.typeOfMerge = typeOfMerge;
		this.conditionName = conditionName;
		this.manualMergeColumnName = manualMergeColumnName;
		this.manualMergeColumnValues = manualMergeColumnValues;
		this.typeOfXref = typeOfXref;
		this.existingSelectedXrefValue = existingSelectedXrefValue;
		this.selectedXrefKeyValue = selectedXrefKeyValue;
		this.applicableDate = applicableDate;
		this.stats = stats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.modification = modification;
		this.xrefKey = xrefKey;
	}

	public Integer getIlId() {
		return ilId;
	}

	public void setIlId(Integer ilId) {
		this.ilId = ilId;
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

	public String getManualMergeColumnName() {
		return manualMergeColumnName;
	}

	public void setManualMergeColumnName(String manualMergeColumnName) {
		this.manualMergeColumnName = manualMergeColumnName;
	}

	public String getManualMergeColumnValues() {
		return manualMergeColumnValues;
	}

	public void setManualMergeColumnValues(String manualMergeColumnValues) {
		this.manualMergeColumnValues = manualMergeColumnValues;
	}

	public String getTypeOfXref() {
		return typeOfXref;
	}

	public void setTypeOfXref(String typeOfXref) {
		this.typeOfXref = typeOfXref;
	}

	public String getExistingSelectedXrefValue() {
		return existingSelectedXrefValue;
	}

	public void setExistingSelectedXrefValue(String existingSelectedXrefValue) {
		this.existingSelectedXrefValue = existingSelectedXrefValue;
	}

	public String getSelectedXrefKeyValue() {
		return selectedXrefKeyValue;
	}

	public void setSelectedXrefKeyValue(String selectedXrefKeyValue) {
		this.selectedXrefKeyValue = selectedXrefKeyValue;
	}

	public String getBulkMergeReferenceFields() {
		return bulkMergeReferenceFields;
	}

	public void setBulkMergeReferenceFields(String bulkMergeReferenceFields) {
		this.bulkMergeReferenceFields = bulkMergeReferenceFields;
	}

	public String getBulkMergeXreferenceFields() {
		return bulkMergeXreferenceFields;
	}

	public void setBulkMergeXreferenceFields(String bulkMergeXreferenceFields) {
		this.bulkMergeXreferenceFields = bulkMergeXreferenceFields;
	}

	public int getSourceFileInfoId() {
		return sourceFileInfoId;
	}

	public void setSourceFileInfoId(int sourceFileInfoId) {
		this.sourceFileInfoId = sourceFileInfoId;
	}

	public String getAutoMergeColumns() {
		return autoMergeColumns;
	}

	public void setAutoMergeColumns(String autoMergeColumns) {
		this.autoMergeColumns = autoMergeColumns;
	}

	public String getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
	}

	public String getStats() {
		return stats;
	}

	public void setStats(String stats) {
		this.stats = stats;
	}

	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getDimension() {
		return dimension;
	}


	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getConditionObject()
	{
		return conditionObject;
	}
	public void setConditionObject(String conditionObject)
	{
		this.conditionObject = conditionObject;
	}

	public boolean isMergeStatus()
	{
		return mergeStatus;
	}


	public void setMergeStatus(boolean mergeStatus)
	{
		this.mergeStatus = mergeStatus;
	}


	public boolean isActive()
	{
		return active;
	}


	public void setActive(boolean active)
	{
		this.active = active;
	}


	public long getXrefKey()
	{
		return xrefKey;
	}


	public void setXrefKey(long xrefKey)
	{
		this.xrefKey = xrefKey;
	}

	public String getXrefExecutionType()
	{
		return xrefExecutionType;
	}


	public void setXrefExecutionType(String xrefExecutionType)
	{
		this.xrefExecutionType = xrefExecutionType;
	}


	@Override
	public String toString()
	{
		return "CrossReferenceLogs [ilId=" + ilId + ", crossReferenceOption=" + crossReferenceOption + ", typeOfMerge=" + typeOfMerge + ", conditionName=" + conditionName + ", manualMergeColumnName=" + manualMergeColumnName + ", manualMergeColumnValues=" + manualMergeColumnValues + ", typeOfXref="
				+ typeOfXref + ", existingSelectedXrefValue=" + existingSelectedXrefValue + ", selectedXrefKeyValue=" + selectedXrefKeyValue + ", bulkMergeReferenceFields=" + bulkMergeReferenceFields + ", bulkMergeXreferenceFields=" + bulkMergeXreferenceFields + ", sourceFileInfoId="
				+ sourceFileInfoId + ", autoMergeColumns=" + autoMergeColumns + ", applicableDate=" + applicableDate + ", stats=" + stats + ", startDate=" + startDate + ", endDate=" + endDate + ", modification=" + modification + ", id=" + id + ", dimension=" + dimension + ", conditionObject="
				+ conditionObject + ", mergeStatus=" + mergeStatus + ", active=" + active + ", xrefKey=" + xrefKey + ", xrefExecutionType=" + xrefExecutionType + "]";
	}

	
}
