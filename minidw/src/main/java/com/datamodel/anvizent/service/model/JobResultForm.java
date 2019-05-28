package com.datamodel.anvizent.service.model;

public class JobResultForm {

	private String packageName;
	private String packageId;
	private String fromDate;
	private String toDate;
	private Integer ilId;
	private String ilName;
	private String pageMode; 
	private String dlId;
	private String dlName;
	private long hierarchyId;
	private String hierarchyName;
	private int xrefConditionId;
	private String validationScriptname;
	private Integer scriptId;
	private String xrefConditionName;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	 
	public String getIlName() {
		return ilName;
	}
	public void setIlName(String ilName) {
		this.ilName = ilName;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public String getDlId() {
		return dlId;
	}
	public void setDlId(String dlId) {
		this.dlId = dlId;
	}
	public String getDlName() {
		return dlName;
	}
	public void setDlName(String dlName) {
		this.dlName = dlName;
	}
	public long getHierarchyId() {
		return hierarchyId;
	}
	public void setHierarchyId(long hierarchyId) {
		this.hierarchyId = hierarchyId;
	}
	public String getHierarchyName() {
		return hierarchyName;
	}
	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}
	public String getValidationScriptname() {
		return validationScriptname;
	}
	public void setValidationScriptname(String validationScriptname) {
		this.validationScriptname = validationScriptname;
	}
	public Integer getScriptId() {
		return scriptId;
	}
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	public int getXrefConditionId()
	{
		return xrefConditionId;
	}
	public void setXrefConditionId(int xrefConditionId)
	{
		this.xrefConditionId = xrefConditionId;
	}
	public String getXrefConditionName()
	{
		return xrefConditionName;
	}
	public void setXrefConditionName(String xrefConditionName)
	{
		this.xrefConditionName = xrefConditionName;
	}
	@Override
	public String toString() {
		return "JobResultForm [packageName=" + packageName + ", packageId=" + packageId + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", ilId=" + ilId + ", ilName=" + ilName + ", pageMode=" + pageMode + ", dlId="
				+ dlId + ", dlName=" + dlName + ", hierarchyId=" + hierarchyId + ", hierarchyName=" + hierarchyName
				+ ", validationScriptname=" + validationScriptname + ", scriptId=" + scriptId + ", xrefConditionId="
				+ xrefConditionId + ", xrefConditionName=" + xrefConditionName + "]";
	}
	 
	 
		
}
