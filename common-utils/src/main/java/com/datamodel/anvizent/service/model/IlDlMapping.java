package com.datamodel.anvizent.service.model;

public class IlDlMapping
{
	private int id;
	private int ilId;
	private int dlId;
	private boolean active;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getIlId()
	{
		return ilId;
	}

	public void setIlId(int ilId)
	{
		this.ilId = ilId;
	}

	public int getDlId()
	{
		return dlId;
	}

	public void setDlId(int dlId)
	{
		this.dlId = dlId;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public String toString()
	{
		return "IlDlMapping [id=" + id + ", ilId=" + ilId + ", dlId=" + dlId + ", active=" + active + "]";
	}

}
