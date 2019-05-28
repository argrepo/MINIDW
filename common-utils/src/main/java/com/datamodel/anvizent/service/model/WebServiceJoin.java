package com.datamodel.anvizent.service.model;

public class WebServiceJoin
{

	private String webServiceUrl;
	private String webServiceMethodType;
	private String responseObjectName;
	private String webServiceApiName;
	private String apiPathParams;
	private String apiRequestParams;
	private String apiBodyParams;
	private Boolean paginationRequired;
	private String paginationRequestParamsData;
	private Boolean incrementalUpdate;
	private String incrementalUpdateparamdata;
	private String responseColumnObjectName;
	private Boolean validateOrPreview = false;
	private Integer wsMappingId;
	private String baseUrl;
	private Boolean baseUrlRequired = false;
	private String paginationType;
	private String soapBodyElement;
	private String retryPaginationData;

	public String getWebServiceUrl()
	{
		return webServiceUrl;
	}

	public void setWebServiceUrl(String webServiceUrl)
	{
		this.webServiceUrl = webServiceUrl;
	}

	public String getWebServiceMethodType()
	{
		return webServiceMethodType;
	}

	public void setWebServiceMethodType(String webServiceMethodType)
	{
		this.webServiceMethodType = webServiceMethodType;
	}

	public String getResponseObjectName()
	{
		return responseObjectName;
	}

	public void setResponseObjectName(String responseObjectName)
	{
		this.responseObjectName = responseObjectName;
	}

	public String getWebServiceApiName()
	{
		return webServiceApiName;
	}

	public void setWebServiceApiName(String webServiceApiName)
	{
		this.webServiceApiName = webServiceApiName;
	}

	public String getApiPathParams()
	{
		return apiPathParams;
	}

	public void setApiPathParams(String apiPathParams)
	{
		this.apiPathParams = apiPathParams;
	}

	public String getApiRequestParams()
	{
		return apiRequestParams;
	}

	public void setApiRequestParams(String apiRequestParams)
	{
		this.apiRequestParams = apiRequestParams;
	}

	public String getApiBodyParams()
	{
		return apiBodyParams;
	}

	public void setApiBodyParams(String apiBodyParams)
	{
		this.apiBodyParams = apiBodyParams;
	}

	public Boolean getIncrementalUpdate()
	{
		return incrementalUpdate;
	}

	public void setIncrementalUpdate(Boolean incrementalUpdate)
	{
		this.incrementalUpdate = incrementalUpdate;
	}

	public String getIncrementalUpdateparamdata()
	{
		return incrementalUpdateparamdata;
	}

	public void setIncrementalUpdateparamdata(String incrementalUpdateparamdata)
	{
		this.incrementalUpdateparamdata = incrementalUpdateparamdata;
	}

	public Integer getWsMappingId()
	{
		return wsMappingId;
	}

	public void setWsMappingId(Integer wsMappingId)
	{
		this.wsMappingId = wsMappingId;
	}

	public String getResponseColumnObjectName()
	{
		return responseColumnObjectName;
	}

	public void setResponseColumnObjectName(String responseColumnObjectName)
	{
		this.responseColumnObjectName = responseColumnObjectName;
	}

	public Boolean getPaginationRequired()
	{
		return paginationRequired;
	}

	public void setPaginationRequired(Boolean paginationRequired)
	{
		this.paginationRequired = paginationRequired;
	}

	public String getPaginationRequestParamsData()
	{
		return paginationRequestParamsData;
	}

	public void setPaginationRequestParamsData(String paginationRequestParamsData)
	{
		this.paginationRequestParamsData = paginationRequestParamsData;
	}

	public Boolean getValidateOrPreview()
	{
		return validateOrPreview;
	}

	public void setValidateOrPreview(Boolean validateOrPreview)
	{
		this.validateOrPreview = validateOrPreview;
	}

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	public Boolean getBaseUrlRequired()
	{
		return baseUrlRequired;
	}

	public void setBaseUrlRequired(Boolean baseUrlRequired)
	{
		this.baseUrlRequired = baseUrlRequired;
	}

	public String getPaginationType()
	{
		return paginationType;
	}

	public void setPaginationType(String paginationType)
	{
		this.paginationType = paginationType;
	}

	public String getSoapBodyElement()
	{
		return soapBodyElement;
	}

	public void setSoapBodyElement(String soapBodyElement)
	{
		this.soapBodyElement = soapBodyElement;
	}

	public String getRetryPaginationData()
	{
		return retryPaginationData;
	}

	public void setRetryPaginationData(String retryPaginationData)
	{
		this.retryPaginationData = retryPaginationData;
	}

	@Override
	public String toString()
	{
		return "WebServiceJoin [webServiceUrl=" + webServiceUrl + ", webServiceMethodType=" + webServiceMethodType + ", responseObjectName=" + responseObjectName + ", webServiceApiName=" + webServiceApiName + ", apiPathParams=" + apiPathParams + ", apiRequestParams=" + apiRequestParams
				+ ", apiBodyParams=" + apiBodyParams + ", paginationRequired=" + paginationRequired + ", paginationRequestParamsData=" + paginationRequestParamsData + ", incrementalUpdate=" + incrementalUpdate + ", incrementalUpdateparamdata=" + incrementalUpdateparamdata
				+ ", responseColumnObjectName=" + responseColumnObjectName + ", validateOrPreview=" + validateOrPreview + ", wsMappingId=" + wsMappingId + ", baseUrl=" + baseUrl + ", baseUrlRequired=" + baseUrlRequired + ", paginationType=" + paginationType + ", soapBodyElement=" + soapBodyElement
				+ ", retryPaginationData=" + retryPaginationData + "]";
	}

}
