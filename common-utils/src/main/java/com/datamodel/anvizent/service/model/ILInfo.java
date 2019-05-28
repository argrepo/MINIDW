package com.datamodel.anvizent.service.model;

import java.util.List;

public class ILInfo {
	private int iL_id;
	private String iL_name;
	private String iLType;
	private String iL_table_name;
	private String xref_il_table_name;
	private String description;
	private String iL_schema;
	private String sharedDLIds;
	private String iLStatus;
	private boolean isStandard;
	private String jobName;
	private String srcFileContextParamKey;
	private String targetTableContextParamKey;
	private DLInfo dLInfo;
	private Modification modification;
	private String purgeScript;
	private List<String> purgeScripts;
	private Boolean isActive;
	private String jobFileNames;
	private Boolean isDefaultIL;
	private String xRef_IL_name;
	private String dependencyJars;
	private String version;
	private int wsApiCount;
	private List<Column> columns;
	private List<ILConnectionMapping> ilSources;
	private String jobExecutionType;
	private long jobTagId;
	private long masterParameterId;
	private long loadParameterId;
	
	
	public ILInfo() {
	}
	public ILInfo(int iL_id, String iL_name) {
		super();
		this.iL_id = iL_id;
		this.iL_name = iL_name;
	}
	public int getiL_id() {
		return iL_id;
	}
	public void setiL_id(int iL_id) {
		this.iL_id = iL_id;
	}
	public String getiL_name() {
		return iL_name;
	}
	public void setiL_name(String iL_name) {
		this.iL_name = iL_name;
	}	
	public String getiLType() {
		return iLType;
	}
	public void setiLType(String iLType) {
		this.iLType = iLType;
	}
	public String getiL_table_name() {
		return iL_table_name;
	}
	public void setiL_table_name(String iL_table_name) {
		this.iL_table_name = iL_table_name;
	}
	public String getXref_il_table_name() {
		return xref_il_table_name;
	}
	public void setXref_il_table_name(String xref_il_table_name) {
		this.xref_il_table_name = xref_il_table_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getiL_schema() {
		return iL_schema;
	}
	public void setiL_schema(String iL_schema) {
		this.iL_schema = iL_schema;
	}
	public DLInfo getdLInfo() {
		return dLInfo;
	}
	public void setdLInfo(DLInfo dLInfo) {
		this.dLInfo = dLInfo;
	}
	public boolean isStandard() {
		return isStandard;
	}
	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}
	
	public String getSharedDLIds() {
		return sharedDLIds;
	}
	public void setSharedDLIds(String sharedDLIds) {
		this.sharedDLIds = sharedDLIds;
	}
	
	public String getiLStatus() {
		return iLStatus;
	}
	public void setiLStatus(String iLStatus) {
		this.iLStatus = iLStatus;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getSrcFileContextParamKey() {
		return srcFileContextParamKey;
	}
	public void setSrcFileContextParamKey(String srcFileContextParamKey) {
		this.srcFileContextParamKey = srcFileContextParamKey;
	}
	public String getTargetTableContextParamKey() {
		return targetTableContextParamKey;
	}
	public void setTargetTableContextParamKey(String targetTableContextParamKey) {
		this.targetTableContextParamKey = targetTableContextParamKey;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public String getPurgeScript() {
		return purgeScript;
	}
	public void setPurgeScript(String purgeScript) {
		this.purgeScript = purgeScript;
	}
	public List<String> getPurgeScripts() {
		return purgeScripts;
	}
	public void setPurgeScripts(List<String> purgeScripts) {
		this.purgeScripts = purgeScripts;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getJobFileNames() {
		return jobFileNames;
	}
	public void setJobFileNames(String jobFileNames) {
		this.jobFileNames = jobFileNames;
	}
	
	public Boolean getIsDefaultIL() {
		return isDefaultIL;
	}
	public void setIsDefaultIL(Boolean isDefaultIL) {
		this.isDefaultIL = isDefaultIL;
	}
	
	public String getxRef_IL_name() {
		return xRef_IL_name;
	}
	public void setxRef_IL_name(String xRef_IL_name) {
		this.xRef_IL_name = xRef_IL_name;
	}
	public String getDependencyJars() {
		return dependencyJars;
	}
	public void setDependencyJars(String dependencyJars) {
		this.dependencyJars = dependencyJars;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getWsApiCount() {
		return wsApiCount;
	}
	public void setWsApiCount(int wsApiCount) {
		this.wsApiCount = wsApiCount;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public List<ILConnectionMapping> getIlSources() {
		return ilSources;
	}
	public void setIlSources(List<ILConnectionMapping> ilSources) {
		this.ilSources = ilSources;
	}
	public String getJobExecutionType() {
		return jobExecutionType;
	}
	public void setJobExecutionType(String jobExecutionType) {
		this.jobExecutionType = jobExecutionType;
	}
	public long getJobTagId() {
		return jobTagId;
	}
	public void setJobTagId(long jobTagId) {
		this.jobTagId = jobTagId;
	}
	public long getMasterParameterId() {
		return masterParameterId;
	}
	public void setMasterParameterId(long masterParameterId) {
		this.masterParameterId = masterParameterId;
	}
	public long getLoadParameterId() {
		return loadParameterId;
	}
	public void setLoadParameterId(long loadParameterId) {
		this.loadParameterId = loadParameterId;
	}
	@Override
	public String toString() {
		return "ILInfo [iL_id=" + iL_id + ", iL_name=" + iL_name + ", iLType=" + iLType + ", iL_table_name="
				+ iL_table_name + ", xref_il_table_name=" + xref_il_table_name + ", description=" + description
				+ ", iL_schema=" + iL_schema + ", sharedDLIds=" + sharedDLIds + ", iLStatus=" + iLStatus
				+ ", isStandard=" + isStandard + ", jobName=" + jobName + ", srcFileContextParamKey="
				+ srcFileContextParamKey + ", targetTableContextParamKey=" + targetTableContextParamKey + ", dLInfo="
				+ dLInfo + ", modification=" + modification + ", purgeScript=" + purgeScript + ", purgeScripts="
				+ purgeScripts + ", isActive=" + isActive + ", jobFileNames=" + jobFileNames + ", isDefaultIL="
				+ isDefaultIL + ", xRef_IL_name=" + xRef_IL_name + ", dependencyJars=" + dependencyJars + ", version="
				+ version + ", wsApiCount=" + wsApiCount + ", columns=" + columns + ", ilSources=" + ilSources
				+ ", jobExecutionType=" + jobExecutionType + ", jobTagId=" + jobTagId + ", masterParameterId="
				+ masterParameterId + ", loadParameterId=" + loadParameterId + "]";
	}
	
	
}
