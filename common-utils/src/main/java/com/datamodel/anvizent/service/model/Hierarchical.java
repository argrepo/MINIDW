package com.datamodel.anvizent.service.model;

public class Hierarchical {
	private long id;
	private String name;
	private String description;
	private String hierarchicalFormData;
	private boolean active;
	private String measures;
	private String dimensions;
	private long associationId;
	private int ilId;
	private int dlId;
	private String associationName;
	private String tableName;
	private Modification modification;
	private String hierarchicalLevelData;
	private boolean financialTemplate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHierarchicalFormData() {
		return hierarchicalFormData;
	}
	public void setHierarchicalFormData(String hierarchicalFormData) {
		this.hierarchicalFormData = hierarchicalFormData;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public String getAssociationName() {
		return associationName;
	}
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
	public long getAssociationId() {
		return associationId;
	}
	public void setAssociationId(long associationId) {
		this.associationId = associationId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getIlId() {
		return ilId;
	}
	public void setIlId(int ilId) {
		this.ilId = ilId;
	}
	public int getDlId() {
		return dlId;
	}
	public void setDlId(int dlId) {
		this.dlId = dlId;
	}
	public String getHierarchicalLevelData() {
		return hierarchicalLevelData;
	}
	public void setHierarchicalLevelData(String hierarchicalLevelData) {
		this.hierarchicalLevelData = hierarchicalLevelData;
	}
	public boolean isFinancialTemplate()
	{
		return financialTemplate;
	}
	public void setFinancialTemplate(boolean financialTemplate)
	{
		this.financialTemplate = financialTemplate;
	}
	@Override
	public String toString()
	{
		return "Hierarchical [id=" + id + ", name=" + name + ", description=" + description + ", hierarchicalFormData=" + hierarchicalFormData + ", active=" + active + ", measures=" + measures + ", dimensions=" + dimensions + ", associationId=" + associationId + ", ilId=" + ilId + ", dlId=" + dlId
				+ ", associationName=" + associationName + ", tableName=" + tableName + ", modification=" + modification + ", hierarchicalLevelData=" + hierarchicalLevelData + ", financialTemplate=" + financialTemplate + "]";
	}
	
}
