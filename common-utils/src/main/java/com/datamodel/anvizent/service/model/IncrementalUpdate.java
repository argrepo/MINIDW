package com.datamodel.anvizent.service.model;

public class IncrementalUpdate {

	Integer id;
	Integer dataSourceId;
	String typeOfSource;
	Integer ilId;
	String incDateFromSource;
	Modification modification;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getTypeOfSource() {
		return typeOfSource;
	}
	public void setTypeOfSource(String typeOfSource) {
		this.typeOfSource = typeOfSource;
	}
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public String getIncDateFromSource() {
		return incDateFromSource;
	}
	public void setIncDateFromSource(String incDateFromSource) {
		this.incDateFromSource = incDateFromSource;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	@Override
	public String toString() {
		return "IncrementalUpdate [id=" + id + ", dataSourceId=" + dataSourceId + ", typeOfSource=" + typeOfSource + ", ilId=" + ilId + ", incDateFromSource=" + incDateFromSource
				+ ", modification=" + modification + "]";
	}
	
}
