package com.datamodel.anvizent.service.model;

public class ERP
{
	private int id;
	private String name;
	private String version;
	private String description;
	private Boolean isActive;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Boolean getIsActive()
	{
		return isActive;
	}
	public void setIsActive(Boolean isActive)
	{
		this.isActive = isActive;
	}
	@Override
	public String toString()
	{
		return "ERP [id=" + id + ", name=" + name + ", version=" + version + ", description=" + description + ", isActive=" + isActive + ", getId()=" + getId() + ", getName()=" + getName() + ", getVersion()=" + getVersion() + ", getDescription()=" + getDescription() + ", getIsActive()="
				+ getIsActive() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
