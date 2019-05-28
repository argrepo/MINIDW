package com.datamodel.anvizent.service.model;

public class Industry {

	private int id;
	private String name;
	private Modification modification;
	private String description;
	private Boolean isActive;
	private Boolean isDefault;
	
	public Industry(){}
	
	public Industry(int id){
		this.id= id;
	}
	public Industry(int id, String name){
		this.id= id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "Industry [id=" + id + ", name=" + name + ", modification=" + modification + ", description="
				+ description + ", isActive=" + isActive + ", isDefault=" + isDefault + "]";
	}
	
}
