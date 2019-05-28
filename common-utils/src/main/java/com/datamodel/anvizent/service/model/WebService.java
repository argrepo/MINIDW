package com.datamodel.anvizent.service.model;

import java.util.List;
import java.util.Map;

public class WebService {
	
	private Integer wsConId;
	private String url;
	private List<String> paramKey;
	private List<String> paramValue;
	private String requestMethod;
	private String headers;
	private String requestParameters; 
	private String authenticationUrl;
	private Boolean authenticationRequired;
	private Boolean cookieRequired;
	private String apiName;
	private int ilId;
	private int industryId;
	private int packageId;
	private List<Map<String,String>> pathParams;
	private List<Map<String,String>> requestBodyParams;
	private String email;
	private String password;
	private List<Map<String,String>> authRequsetParams;
	private String responseObjName;
	private String tokenName;
    private String authentication_Request_Params;
    private String Web_Service_Path_Params;
    private String web_service_id;
    private String authentication_Method_Type;
    private String web_Service_Method_Type;
    private String webserviceName;
    private String web_service_sub_url;
    private String web_service_sub_url_param;
    private String responseColumnObjName;
    private String Web_Service_Request_Body_Params;
    private List<String> requsetBodyParamKey;
	private List<String> requsetBodyParamKeyParamValue;
    private int clientId;
    private Boolean isChangeAuthDetails;
    private Modification modification;
    private OAuth2 oauth2;
    private String userId;
    private String authCode;
    private String userTokenName;
    private List<WebServiceJoin> webServiceJoin;
    private Integer authenticationTypeId;
    private List<Map<String,String>> headerKeyValues;
    private String apiPathParams;
    private String apiBodyParams;
    private Boolean incrementalUpdate;
    private String incrementalUpdateparamdata;
	private Boolean paginationRequired;
	private String paginationRequestParamsData;
	private Boolean validateOrPreview=false;
    private String baseUrl;
    private Boolean baseUrlRequired=false;
    private String paginationType;
    private String soapBodyElement;
    private String defaultMapping;
    private String fileType;
    
	public Integer getWsConId() {
		return wsConId;
	}
	public void setWsConId(Integer wsConId) {
		this.wsConId = wsConId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getParamKey() {
		return paramKey;
	}
	public void setParamKey(List<String> paramKey) {
		this.paramKey = paramKey;
	}
	public List<String> getParamValue() {
		return paramValue;
	}
	public void setParamValue(List<String> paramValue) {
		this.paramValue = paramValue;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getRequestParameters() {
		return requestParameters;
	}
	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}
	public String getAuthenticationUrl() {
		return authenticationUrl;
	}
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}
	public Boolean getAuthenticationRequired() {
		return authenticationRequired;
	}
	public void setAuthenticationRequired(Boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}
	public Boolean getCookieRequired() {
		return cookieRequired;
	}
	public void setCookieRequired(Boolean cookieRequired) {
		this.cookieRequired = cookieRequired;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public int getIlId() {
		return ilId;
	}
	public void setIlId(int ilId) {
		this.ilId = ilId;
	}
	public int getIndustryId() {
		return industryId;
	}
	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public List<Map<String, String>> getPathParams() {
		return pathParams;
	}
	public void setPathParams(List<Map<String, String>> pathParams) {
		this.pathParams = pathParams;
	}
	public List<Map<String, String>> getRequestBodyParams() {
		return requestBodyParams;
	}
	public void setRequestBodyParams(List<Map<String, String>> requestBodyParams) {
		this.requestBodyParams = requestBodyParams;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Map<String, String>> getAuthRequsetParams() {
		return authRequsetParams;
	}
	public void setAuthRequsetParams(List<Map<String, String>> authRequsetParams) {
		this.authRequsetParams = authRequsetParams;
	}
	public String getResponseObjName() {
		return responseObjName;
	}
	public void setResponseObjName(String responseObjName) {
		this.responseObjName = responseObjName;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public String getAuthentication_Request_Params() {
		return authentication_Request_Params;
	}
	public void setAuthentication_Request_Params(String authentication_Request_Params) {
		this.authentication_Request_Params = authentication_Request_Params;
	}
	public String getWeb_Service_Path_Params() {
		return Web_Service_Path_Params;
	}
	public void setWeb_Service_Path_Params(String web_Service_Path_Params) {
		Web_Service_Path_Params = web_Service_Path_Params;
	}
	public String getWeb_service_id() {
		return web_service_id;
	}
	public void setWeb_service_id(String web_service_id) {
		this.web_service_id = web_service_id;
	}
	public String getAuthentication_Method_Type() {
		return authentication_Method_Type;
	}
	public void setAuthentication_Method_Type(String authentication_Method_Type) {
		this.authentication_Method_Type = authentication_Method_Type;
	}
	public String getWeb_Service_Method_Type() {
		return web_Service_Method_Type;
	}
	public void setWeb_Service_Method_Type(String web_Service_Method_Type) {
		this.web_Service_Method_Type = web_Service_Method_Type;
	}
	public String getWebserviceName() {
		return webserviceName;
	}
	public void setWebserviceName(String webserviceName) {
		this.webserviceName = webserviceName;
	}
	public String getWeb_service_sub_url() {
		return web_service_sub_url;
	}
	public void setWeb_service_sub_url(String web_service_sub_url) {
		this.web_service_sub_url = web_service_sub_url;
	}
	public String getWeb_service_sub_url_param() {
		return web_service_sub_url_param;
	}
	public void setWeb_service_sub_url_param(String web_service_sub_url_param) {
		this.web_service_sub_url_param = web_service_sub_url_param;
	}
	public String getResponseColumnObjName() {
		return responseColumnObjName;
	}
	public void setResponseColumnObjName(String responseColumnObjName) {
		this.responseColumnObjName = responseColumnObjName;
	}
	public String getWeb_Service_Request_Body_Params() {
		return Web_Service_Request_Body_Params;
	}
	public void setWeb_Service_Request_Body_Params(String web_Service_Request_Body_Params) {
		Web_Service_Request_Body_Params = web_Service_Request_Body_Params;
	}
	public List<String> getRequsetBodyParamKey() {
		return requsetBodyParamKey;
	}
	public void setRequsetBodyParamKey(List<String> requsetBodyParamKey) {
		this.requsetBodyParamKey = requsetBodyParamKey;
	}
	public List<String> getRequsetBodyParamKeyParamValue() {
		return requsetBodyParamKeyParamValue;
	}
	public void setRequsetBodyParamKeyParamValue(List<String> requsetBodyParamKeyParamValue) {
		this.requsetBodyParamKeyParamValue = requsetBodyParamKeyParamValue;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public Boolean getIsChangeAuthDetails() {
		return isChangeAuthDetails;
	}
	public void setIsChangeAuthDetails(Boolean isChangeAuthDetails) {
		this.isChangeAuthDetails = isChangeAuthDetails;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public OAuth2 getOauth2() {
		return oauth2;
	}
	public void setOauth2(OAuth2 oauth2) {
		this.oauth2 = oauth2;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getUserTokenName() {
		return userTokenName;
	}
	public void setUserTokenName(String userTokenName) {
		this.userTokenName = userTokenName;
	}
	public List<WebServiceJoin> getWebServiceJoin() {
		return webServiceJoin;
	}
	public void setWebServiceJoin(List<WebServiceJoin> webServiceJoin) {
		this.webServiceJoin = webServiceJoin;
	}
	public Integer getAuthenticationTypeId() {
		return authenticationTypeId;
	}
	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}
	public List<Map<String, String>> getHeaderKeyValues() {
		return headerKeyValues;
	}
	public void setHeaderKeyValues(List<Map<String, String>> headerKeyValues) {
		this.headerKeyValues = headerKeyValues;
	}
	public String getApiPathParams() {
		return apiPathParams;
	}
	public void setApiPathParams(String apiPathParams) {
		this.apiPathParams = apiPathParams;
	}
	public String getApiBodyParams() {
		return apiBodyParams;
	}
	public void setApiBodyParams(String apiBodyParams) {
		this.apiBodyParams = apiBodyParams;
	}
	public Boolean getIncrementalUpdate() {
		return incrementalUpdate;
	}
	public void setIncrementalUpdate(Boolean incrementalUpdate) {
		this.incrementalUpdate = incrementalUpdate;
	}
	public String getIncrementalUpdateparamdata() {
		return incrementalUpdateparamdata;
	}
	public void setIncrementalUpdateparamdata(String incrementalUpdateparamdata) {
		this.incrementalUpdateparamdata = incrementalUpdateparamdata;
	}
	public Boolean getPaginationRequired() {
		return paginationRequired;
	}
	public void setPaginationRequired(Boolean paginationRequired) {
		this.paginationRequired = paginationRequired;
	}
	public String getPaginationRequestParamsData() {
		return paginationRequestParamsData;
	}
	public void setPaginationRequestParamsData(String paginationRequestParamsData) {
		this.paginationRequestParamsData = paginationRequestParamsData;
	}
	public Boolean getValidateOrPreview() {
		return validateOrPreview;
	}
	public void setValidateOrPreview(Boolean validateOrPreview) {
		this.validateOrPreview = validateOrPreview;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public Boolean getBaseUrlRequired() {
		return baseUrlRequired;
	}
	public void setBaseUrlRequired(Boolean baseUrlRequired) {
		this.baseUrlRequired = baseUrlRequired;
	}
	public String getPaginationType() {
		return paginationType;
	}
	public void setPaginationType(String paginationType) {
		this.paginationType = paginationType;
	}
	public String getSoapBodyElement() {
		return soapBodyElement;
	}
	public void setSoapBodyElement(String soapBodyElement) {
		this.soapBodyElement = soapBodyElement;
	}
	public String getDefaultMapping() {
		return defaultMapping;
	}
	public void setDefaultMapping(String defaultMapping) {
		this.defaultMapping = defaultMapping;
	}
	public String getFileType()
	{
		return fileType;
	}
	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}
	@Override
	public String toString()
	{
		return "WebService [wsConId=" + wsConId + ", url=" + url + ", paramKey=" + paramKey + ", paramValue=" + paramValue + ", requestMethod=" + requestMethod + ", headers=" + headers + ", requestParameters=" + requestParameters + ", authenticationUrl=" + authenticationUrl
				+ ", authenticationRequired=" + authenticationRequired + ", cookieRequired=" + cookieRequired + ", apiName=" + apiName + ", ilId=" + ilId + ", industryId=" + industryId + ", packageId=" + packageId + ", pathParams=" + pathParams + ", requestBodyParams=" + requestBodyParams
				+ ", email=" + email + ", password=" + password + ", authRequsetParams=" + authRequsetParams + ", responseObjName=" + responseObjName + ", tokenName=" + tokenName + ", authentication_Request_Params=" + authentication_Request_Params + ", Web_Service_Path_Params="
				+ Web_Service_Path_Params + ", web_service_id=" + web_service_id + ", authentication_Method_Type=" + authentication_Method_Type + ", web_Service_Method_Type=" + web_Service_Method_Type + ", webserviceName=" + webserviceName + ", web_service_sub_url=" + web_service_sub_url
				+ ", web_service_sub_url_param=" + web_service_sub_url_param + ", responseColumnObjName=" + responseColumnObjName + ", Web_Service_Request_Body_Params=" + Web_Service_Request_Body_Params + ", requsetBodyParamKey=" + requsetBodyParamKey + ", requsetBodyParamKeyParamValue="
				+ requsetBodyParamKeyParamValue + ", clientId=" + clientId + ", isChangeAuthDetails=" + isChangeAuthDetails + ", modification=" + modification + ", oauth2=" + oauth2 + ", userId=" + userId + ", authCode=" + authCode + ", userTokenName=" + userTokenName + ", webServiceJoin="
				+ webServiceJoin + ", authenticationTypeId=" + authenticationTypeId + ", headerKeyValues=" + headerKeyValues + ", apiPathParams=" + apiPathParams + ", apiBodyParams=" + apiBodyParams + ", incrementalUpdate=" + incrementalUpdate + ", incrementalUpdateparamdata="
				+ incrementalUpdateparamdata + ", paginationRequired=" + paginationRequired + ", paginationRequestParamsData=" + paginationRequestParamsData + ", validateOrPreview=" + validateOrPreview + ", baseUrl=" + baseUrl + ", baseUrlRequired=" + baseUrlRequired + ", paginationType="
				+ paginationType + ", soapBodyElement=" + soapBodyElement + ", defaultMapping=" + defaultMapping + ", fileType=" + fileType + "]";
	}
	
}
