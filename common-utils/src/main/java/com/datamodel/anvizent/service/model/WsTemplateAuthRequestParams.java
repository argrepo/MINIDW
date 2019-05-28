package com.datamodel.anvizent.service.model;

public class WsTemplateAuthRequestParams
{
	private int id;
	private int wsTemplateId;
	private String paramName;
	private boolean mandatory;
	private boolean passwordType;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getWsTemplateId()
	{
		return wsTemplateId;
	}

	public void setWsTemplateId(int wsTemplateId)
	{
		this.wsTemplateId = wsTemplateId;
	}

	public String getParamName()
	{
		return paramName;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	public boolean isMandatory()
	{
		return mandatory;
	}

	public void setMandatory(boolean mandatory)
	{
		this.mandatory = mandatory;
	}

	public boolean isPasswordType()
	{
		return passwordType;
	}

	public void setPasswordType(boolean passwordType)
	{
		this.passwordType = passwordType;
	}

	@Override
	public String toString()
	{
		return "WsTemplateAuthRequestParams [id=" + id + ", wsTemplateId=" + wsTemplateId + ", paramName=" + paramName + ", mandatory=" + mandatory + ", passwordType=" + passwordType + "]";
	}

}
