package com.datamodel.anvizent.data.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.lib.MultiValueHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

public class RestTemplateUtilities {
	private String serviceUrl;
	private String baseUrl;

	@Autowired
	private RestTemplate restTemplate;
	private List<Message> messages = new ArrayList<>();
	private Message message = new Message();
	
	protected static final Log LOG = LogFactory.getLog(RestTemplateUtilities.class);
	public RestTemplateUtilities(String serviceUrl,String baseUrl) {
		this.serviceUrl = serviceUrl;
		this.baseUrl = baseUrl;
		message.setCode("ERROR");
		message.setText("Unable to process");
		messages.add(message);
	}

	public RestTemplateUtilities() {
		this.baseUrl = "";
		message.setCode("ERROR");
		message.setText("Unable to process");
		messages.add(message);
	}
	
	/* for DELETE */


	public DataResponse deleteRestObject(HttpServletRequest request, String url, Object... urlVariables) {
		return deleteRestObject(request, url, DataResponse.class, urlVariables);
	}
	

	public <T> T deleteRestObject(HttpServletRequest request, String url, Class<T> responseType, Object... urlVariables) {
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String clientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		String userClientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID);
		String webServiceContextUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
		String broweserDetails = null;  
		if (request != null) {
			broweserDetails = CommonUtils.getClientBrowserDetails(request); 
		}
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, userClientId, broweserDetails, deploymentType,timeZone);
		return deleteRestObject(customRequest, url, responseType, urlVariables);
	}
	

	public <T> T deleteRestObject(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		ResponseEntity<T> dataResponseEntity = deleteRestEntity(customRequest, url, responseType, urlVariables);
		return dataResponseEntity.getBody();
	}
	

	public <T> ResponseEntity<T> deleteRestEntity(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		
		return exchange(customRequest, url, new MultiValueHashMap(), responseType, HttpMethod.DELETE, urlVariables);
	}
	
	/*end of delete */
	
	
	public DataResponse getRestObject(HttpServletRequest request, String url, Object... urlVariables) {
		return getRestObject(request, url, DataResponse.class, urlVariables);
	}
	
	public <T> T getRestObject(HttpServletRequest request, String url, Class<T> responseType, Object... urlVariables) {
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String clientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		String userClientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID);
		String webServiceContextUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
		String broweserDetails = null;  
		if (request != null) {
			broweserDetails = CommonUtils.getClientBrowserDetails(request); 
		}
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, userClientId, broweserDetails, deploymentType,timeZone);
		return getRestObject(customRequest, url, responseType, urlVariables);
	}

	public DataResponse getRestObject(CustomRequest customRequest, String url, Object... urlVariables) {
		return getRestObject(customRequest, url,  DataResponse.class, urlVariables);
	}

	public <T> T getRestObject(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		ResponseEntity<T> dataResponseEntity = getRestEntity(customRequest, url, responseType, urlVariables);
		return dataResponseEntity.getBody();
	}
	
	
	
	
	
	public <T> ResponseEntity<T> getRestEntity(HttpServletRequest request, String url, Class<T> responseType, Object... urlVariables) {
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String clientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		String userClientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID);
		String webServiceContextUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
		
		String broweserDetails = null;
		if (request != null) {
			broweserDetails = CommonUtils.getClientBrowserDetails(request);
		}
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, userClientId, broweserDetails, deploymentType,timeZone);
		return getRestEntity(customRequest, url, responseType, urlVariables);
	}
	
	public <T> ResponseEntity<T> getRestEntity(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		
		return exchange(customRequest, url, new MultiValueHashMap(), responseType, HttpMethod.GET, urlVariables);
	}

	
	
	
	
	public DataResponse postRestObject(HttpServletRequest request, String url, Object object, Object... urlVariables) {
		return postRestObject(request, url, object, DataResponse.class, urlVariables);
	}

	public <T> T postRestObject(HttpServletRequest request, String url, Object object, Class<T> responseType, Object... urlVariables) {
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String clientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		String userClientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID);
		String webServiceContextUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
		String broweserDetails = null;
		if (request != null) {
			broweserDetails = CommonUtils.getClientBrowserDetails(request);
		}
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, userClientId, broweserDetails, deploymentType,timeZone);
		return postRestObject(customRequest, url, object, responseType, urlVariables);
	}
	
	public <T> T postRestObject(CustomRequest customRequest, String url, Object object, Class<T> responseType, Object... urlVariables) {
		ResponseEntity<T> dataResponseEntity = postRestEntity(customRequest, url, object, responseType, urlVariables);
		return dataResponseEntity.getBody();
	}

	public DataResponse postRestObject(CustomRequest customRequest, String url, Object object, Object... urlVariables) {
		return postRestObject(customRequest, url, object, DataResponse.class, urlVariables);
	}

	public <T> ResponseEntity<T> postRestEntity(HttpServletRequest request, String url, Object object, Class<T> responseType, Object... urlVariables) {
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String clientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		String userClientId = (String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID);
		String webServiceContextUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
		
		String broweserDetails = null;
		if (request != null) {
			broweserDetails = CommonUtils.getClientBrowserDetails(request);
		}
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, userClientId, broweserDetails, deploymentType,timeZone);
		return postRestEntity(customRequest, url, object, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> postRestEntity(CustomRequest customRequest, String url, Object object, Class<T> responseType, Object... urlVariables) {
		return exchange(customRequest, url, object, responseType, HttpMethod.POST, urlVariables);
	}
	
	
	public <T> ResponseEntity<T> exchange(CustomRequest customRequest, String url, Object object, Class<T> responseType,
			HttpMethod methodType, Object... urlVariables) {
		return exchange(customRequest, url, object, responseType, methodType, 0, urlVariables);
	}

	public <T> ResponseEntity<T> exchange(CustomRequest customRequest, String url, Object object, Class<T> responseType,
			HttpMethod methodType, int retryCount, Object... urlVariables) {

		try {
			HttpHeaders headers = new HttpHeaders();
			if (urlVariables.length > 0) {
				headers.set("X-Authentication-code", EncryptionServiceImpl.getInstance().encrypt(urlVariables[0] + "#" + new Date()));
				headers.set(Constants.Config.DEPLOYMENT_TYPE, customRequest.getDeploymentType());
				headers.set(Constants.Config.BROWSER_DETAILS, customRequest.getBrowserDetails());
				headers.set(Constants.Config.CSV_SAVE_PATH, getCsvFolderPath());
				headers.set(Constants.Config.HEADER_CLIENT_ID, customRequest.getClientId());
				headers.set(Constants.Config.HEADER_USER_CLIENT_ID, customRequest.getUserClientId());
				headers.set(Constants.Config.TIME_ZONE, customRequest.getTimeZone());
			}
			
			String serviceUrl = this.serviceUrl; 
			if (StringUtils.isNotBlank(customRequest.getWebServiceContextUrl())) {
				serviceUrl = customRequest.getWebServiceContextUrl();
			}

			HttpEntity<?> entity = new HttpEntity<Object>(object, headers);
			return restTemplate.exchange(serviceUrl + baseUrl + url, methodType, entity, responseType, urlVariables);
		} catch (Exception e) {
			retryCount++;
			LOG.error("Retry count  -> " + retryCount ,e);
			LOG.error("url " + url);
			LOG.error("FormData ->  "+object);
			if ( retryCount <= 3) {
				try {
					Thread.sleep(2000);
				} catch (Exception e2) {
					LOG.error("error while in sleep ", e2);
				}
				return exchange(customRequest, url, object, responseType, methodType, retryCount, urlVariables);
			}
			throw new RuntimeException("Unable to connect MinidwWebservices : " +e.getMessage(),e);
			//return new ResponseEntity<T>((T) null, HttpStatus.NO_CONTENT);
		}

	}
	
	private String getCsvFolderPath() {
		String csvFolderPath = null;
		try {
			csvFolderPath = Constants.Config.STORAGE_LOCATION;
		} catch (Exception e) {System.out.println("Error while getting csv save path " + e.getMessage());}
		if ( StringUtils.isBlank(csvFolderPath) ) {
			csvFolderPath = Constants.Temp.getTempFileDir();
		}
		return csvFolderPath;
	}
	

}
