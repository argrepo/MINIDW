package com.datamodel.anvizent.service.model;

import java.util.List;

public class EltJobTagInfo {
	private Long tagId;
	private String tagName;
	private ELTConfigTags globalValues;
	private List<EltJobInfo> jobsList;
	private boolean active;
	private Modification modification;
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public ELTConfigTags getGlobalValues() {
		if (globalValues == null) {
			globalValues = new ELTConfigTags();
		}
		return globalValues;
	}
	public void setGlobalValues(ELTConfigTags globalValues) {
		this.globalValues = globalValues;
	}
	public List<EltJobInfo> getJobsList() {
		return jobsList;
	}
	public void setJobsList(List<EltJobInfo> jobsList) {
		this.jobsList = jobsList;
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
	@Override
	public String toString() {
		return "EltJobTagInfo [tagId=" + tagId + ", tagName=" + tagName + ", globalValues=" + globalValues + ", active="
				+ active + ", modification=" + modification + "]";
	}
	
	
}
