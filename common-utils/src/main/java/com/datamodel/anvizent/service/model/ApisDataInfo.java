package com.datamodel.anvizent.service.model;

public class ApisDataInfo {
	private int id;
	private String apiName;
	private String endPointUrl;
	private String methodType;
	private String apiQuery;
	private String apiDescription;
	private int userId;
	private Boolean active;
	private String testLink;
	private Modification modification;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getEndPointUrl() {
		return endPointUrl;
	}
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public String getApiQuery() {
		return apiQuery;
	}
	public void setApiQuery(String apiQuery) {
		this.apiQuery = apiQuery;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public String getApiDescription() {
		return apiDescription;
	}
	public void setApiDescription(String apiDescription) {
		this.apiDescription = apiDescription;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getTestLink() {
		return testLink;
	}
	public void setTestLink(String testLink) {
		this.testLink = testLink;
	}
	@Override
	public String toString() {
		return "ApisDataInfo [id=" + id + ", apiName=" + apiName + ", endPointUrl=" + endPointUrl + ", methodType="
				+ methodType + ", apiQuery=" + apiQuery + ", apiDescription=" + apiDescription + ", userId=" + userId
				+ ", active=" + active + ", testLink=" + testLink + ", modification=" + modification + "]";
	}
	
}
