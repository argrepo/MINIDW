package com.datamodel.anvizent.service.model;

public class AIModel {
	
	private Integer id;
	private String aIModelName;
	private String aiModelType;
	private String aiContextParameters;
	private Modification modification;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getaIModelName() {
		return aIModelName;
	}
	public void setaIModelName(String aIModelName) {
		this.aIModelName = aIModelName;
	}
	public String getAiModelType() {
		return aiModelType;
	}
	public void setAiModelType(String aiModelType) {
		this.aiModelType = aiModelType;
	}
	public String getAiContextParameters() {
		return aiContextParameters;
	}
	public void setAiContextParameters(String aiContextParameters) {
		this.aiContextParameters = aiContextParameters;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	@Override
	public String toString() {
		return "AIModel [id=" + id + ", aIModelName=" + aIModelName + ", aiModelType=" + aiModelType
				+ ", aiContextParameters=" + aiContextParameters + ", modification=" + modification + "]";
	}
	
	
	
	

}
