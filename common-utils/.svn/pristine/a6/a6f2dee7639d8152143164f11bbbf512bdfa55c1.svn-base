package com.datamodel.anvizent.service.model;

import java.util.ArrayList;
import java.util.List;

public class EltJobInfo {
	private Long id;
	private Long jobTagId;
	private String jobName;
	private Integer jobSeq;
	private ELTConfigTags configProp;
	private ELTConfigTags valuesProp;
	private ELTConfigTags statsProp;
	private List<ELTConfigTags> derivedComponent;
	private Boolean activeStatus;
	private Modification modification;
	
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getJobTagId() {
		return jobTagId;
	}
	public void setJobTagId(Long jobTagId) {
		this.jobTagId = jobTagId;
	}
	public Integer getJobSeq() {
		return jobSeq;
	}
	public void setJobSeq(Integer jobSeq) {
		this.jobSeq = jobSeq;
	}
	public ELTConfigTags getConfigProp() {
		if (configProp == null) {
			configProp = new ELTConfigTags();
		}
		return configProp;
	}
	public void setConfigProp(ELTConfigTags configProp) {
		this.configProp = configProp;
	}
	public ELTConfigTags getValuesProp() {
		if (valuesProp == null) {
			valuesProp = new ELTConfigTags();
		}
		return valuesProp;
	}
	public void setValuesProp(ELTConfigTags valuesProp) {
		this.valuesProp = valuesProp;
	}
	public ELTConfigTags getStatsProp() {
		if (statsProp == null) {
			statsProp = new ELTConfigTags();
		}
		return statsProp;
	}
	public void setStatsProp(ELTConfigTags statsProp) {
		this.statsProp = statsProp;
	}
	public List<ELTConfigTags> getDerivedComponent() {
		if (derivedComponent == null) {
			derivedComponent = new ArrayList<>();
		}
		return derivedComponent;
	}
	public void setDerivedComponent(List<ELTConfigTags> derivedComponent) {
		this.derivedComponent = derivedComponent;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Boolean getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Override
	public String toString() {
		return "EltJobInfo [id=" + id + ", jobTagId=" + jobTagId + ", jobName=" + jobName + ", jobSeq=" + jobSeq
				+ ", configProp=" + configProp + ", valuesProp=" + valuesProp + ", statsProp=" + statsProp
				+ ", derivedComponent=" + derivedComponent + ", activeStatus=" + activeStatus + ", modification="
				+ modification + "]";
	}
	
}
