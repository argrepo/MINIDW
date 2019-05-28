package com.anvizent.minidw.service.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

public class RestTemplateUtilities {
	private String baseUrl;
	protected static final Log LOGGER = LogFactory.getLog(RestTemplateUtilities.class);
	private RestTemplate restTemplate = new RestTemplate();
	private List<Message> messages = new ArrayList<>();
	private Message message = new Message();

	public RestTemplateUtilities(String baseUrl) {
		SimpleClientHttpRequestFactory rf =
			    (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		rf.setConnectTimeout(1 * 10000);
		this.baseUrl = baseUrl;
		message.setCode("ERROR");
		message.setText("Unable to process");
		messages.add(message);
	}

	public RestTemplateUtilities() {
		SimpleClientHttpRequestFactory rf =
			    (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		rf.setConnectTimeout(1 * 10000);
		this.baseUrl = "";
		message.setCode("ERROR");
		message.setText("Unable to process");
		messages.add(message);
	}

	public DataResponse getRestObject(HttpServletRequest request, String url, Object... urlVariables) {
		return getRestObject(request, url, DataResponse.class, urlVariables);
	}

	public <T> T getRestObject(HttpServletRequest request, String url, Class<T> responseType, Object... urlVariables) {
		CustomRequest customRequest = new CustomRequest();
		return getRestObject(customRequest, url, responseType, urlVariables);
	}

	public DataResponse getRestObject(CustomRequest customRequest, String url, Object... urlVariables) {
		return getRestObject(customRequest, url, DataResponse.class, urlVariables);
	}

	public <T> T getRestObject(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		ResponseEntity<T> dataResponseEntity = getRestEntity(customRequest, url, responseType, urlVariables);
		return dataResponseEntity.getBody();
	}

	public <T> ResponseEntity<T> getRestEntity(HttpServletRequest request, String url, Class<T> responseType, Object... urlVariables) {
		CustomRequest customRequest = new CustomRequest();
		return getRestEntity(customRequest, url, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> getRestEntity(CustomRequest customRequest, String url, Class<T> responseType, Object... urlVariables) {
		return exchange(customRequest, url, new HashMap<>(), responseType, HttpMethod.GET, urlVariables);
	}

	public DataResponse postRestObject(HttpServletRequest request, String url, Object object, Object... urlVariables) {
		return postRestObject(request, url, object, DataResponse.class, urlVariables);
	}

	public <T> T postRestObject(HttpServletRequest request, String url, Object object, Class<T> responseType, Object... urlVariables) {
		CustomRequest customRequest = new CustomRequest();
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
		CustomRequest customRequest = new CustomRequest();
		return postRestEntity(customRequest, url, object, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> postRestEntity(CustomRequest customRequest, String url, Object object, Class<T> responseType, Object... urlVariables) {
		return exchange(customRequest, url, object, responseType, HttpMethod.POST, urlVariables);
	}

	public <T> ResponseEntity<T> exchange(CustomRequest customRequest, String url, Object object, Class<T> responseType, HttpMethod methodType,
			Object... urlVariables) {

		try {
			HttpHeaders headers = new HttpHeaders();
			if (urlVariables.length > 0) {
				// headers.set("X-Authentication-code",
				// EncryptionServiceImpl.getInstance().encrypt(urlVariables[0] +
				// "#" + new Date()));
			}

			HttpEntity<?> entity = new HttpEntity<Object>(object, headers);
			//System.out.println("before calling api " + new Date());
			return restTemplate.exchange(baseUrl + url, methodType, entity, responseType, urlVariables);
		} catch (Exception e) {
			System.out.println("RestTemplateUtilities --> " + e.getMessage() + "url " + url + "FormData ->  " + object);

			return new ResponseEntity<T>((T) null, HttpStatus.NO_CONTENT);
		} finally {
			//System.out.println("after calling api " + new Date());
		}

	}

	public static String getClientBrowserDetails(HttpServletRequest request) {
		StringBuilder clientInformation = new StringBuilder();
		if (request != null) {
			String userAgent = request.getHeader("User-Agent");
			String ipaddress = request.getRemoteAddr();
			clientInformation.append("IP Address: ").append(ipaddress).append("\t");
			clientInformation.append("Browser Details:").append(userAgent).append("");
		}
		return clientInformation.toString();
	}

}
