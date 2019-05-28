package com.datamodel.anvizent.service.model;

import java.util.List;

public class TableScriptsForm {

	private Integer id; 
	private Integer priority;
	private String scriptName;
	private String sqlScript;
	private Modification modification;
	private Boolean is_Update;
	private Boolean is_Active;
	private String pageMode;
	private Integer clientId;
	private Integer clientsInstantScriptId;
	private Integer clientInstantExecutionId;
	private String schemaName;
	private String scriptTypeName;
	private List<TableScripts> tableScriptList;
	private String scriptDescription;
	private String created_Date;
	private String modified_Date;
	private Boolean isDefault;
	private Boolean execution_status=true;
	private String execution_status_msg;
	private String version;
	private List<String> clientIdList;
	private String clientIds;
	private String executionType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getSqlScript() {
		return sqlScript;
	}
	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Boolean getIs_Update() {
		return is_Update;
	}
	public void setIs_Update(Boolean is_Update) {
		this.is_Update = is_Update;
	}
	public Boolean getIs_Active() {
		return is_Active;
	}
	public void setIs_Active(Boolean is_Active) {
		this.is_Active = is_Active;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getScriptTypeName() {
		return scriptTypeName;
	}
	public void setScriptTypeName(String scriptTypeName) {
		this.scriptTypeName = scriptTypeName;
	}
	public List<TableScripts> getTableScriptList() {
		return tableScriptList;
	}
	public void setTableScriptList(List<TableScripts> tableScriptList) {
		this.tableScriptList = tableScriptList;
	}
	public String getScriptDescription() {
		return scriptDescription;
	}
	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
	}
	public String getCreated_Date() {
		return created_Date;
	}
	public void setCreated_Date(String created_Date) {
		this.created_Date = created_Date;
	}
	public String getModified_Date() {
		return modified_Date;
	}
	public void setModified_Date(String modified_Date) {
		this.modified_Date = modified_Date;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Boolean getExecution_status() {
		return execution_status;
	}
	public void setExecution_status(Boolean execution_status) {
		this.execution_status = execution_status;
	}
	public String getExecution_status_msg() {
		return execution_status_msg;
	}
	public void setExecution_status_msg(String execution_status_msg) {
		this.execution_status_msg = execution_status_msg;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<String> getClientIdList() {
		return clientIdList;
	}
	public void setClientIdList(List<String> clientIdList) {
		this.clientIdList = clientIdList;
	}
	public Integer getClientsInstantScriptId() {
		return clientsInstantScriptId;
	}
	public void setClientsInstantScriptId(Integer clientsInstantScriptId) {
		this.clientsInstantScriptId = clientsInstantScriptId;
	}
	public Integer getClientInstantExecutionId() {
		return clientInstantExecutionId;
	}
	public void setClientInstantExecutionId(Integer clientInstantExecutionId) {
		this.clientInstantExecutionId = clientInstantExecutionId;
	}
	
	public String getClientIds() {
		return clientIds;
	}
	public void setClientIds(String clientIds) {
		this.clientIds = clientIds;
	}
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}
	@Override
	public String toString() {
		return "TableScriptsForm [id=" + id + ", priority=" + priority + ", scriptName=" + scriptName + ", sqlScript="
				+ sqlScript + ", modification=" + modification + ", is_Update=" + is_Update + ", is_Active=" + is_Active
				+ ", pageMode=" + pageMode + ", clientId=" + clientId + ", clientsInstantScriptId="
				+ clientsInstantScriptId + ", clientInstantExecutionId=" + clientInstantExecutionId + ", schemaName="
				+ schemaName + ", scriptTypeName=" + scriptTypeName + ", tableScriptList=" + tableScriptList
				+ ", scriptDescription=" + scriptDescription + ", created_Date=" + created_Date + ", modified_Date="
				+ modified_Date + ", isDefault=" + isDefault + ", execution_status=" + execution_status
				+ ", execution_status_msg=" + execution_status_msg + ", version=" + version + ", clientIdList="
				+ clientIdList + ", clientIds=" + clientIds + ", executionType=" + executionType + "]";
	}
	 
}
