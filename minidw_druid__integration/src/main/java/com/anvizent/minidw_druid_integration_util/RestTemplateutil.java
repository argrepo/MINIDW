package com.anvizent.minidw_druid_integration_util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.anvizent.minidw_druid__integration.ErrorHandler;
import com.datamodel.anvizent.errorhandler.CustomRestTemplateResponseErrorHandler;

import flexjson.JSONDeserializer;

public class RestTemplateutil {
	
	private final RestTemplate restTemplate;

	public RestTemplateutil() {
		restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());
	}

	public ResponseEntity<String> exchange(String url, Object requestBody, HttpMethod methodType, HashMap<String, String> headersMap, Object... urlVariables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			if (headersMap != null && !headersMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headersMap.entrySet()) {
					headers.add(entry.getKey(), entry.getValue());
				}
			}
			HttpEntity<?> entity = new HttpEntity<Object>(requestBody, headers);

			return restTemplate.exchange(url, methodType, entity, String.class, urlVariables);
		} catch (Exception e) {
			return new ResponseEntity<String>((String) null, HttpStatus.NO_CONTENT);
		}
	}

	public HashMap<String, Object> getMap(ResponseEntity<String> responseEntity) {
		return new JSONDeserializer<HashMap<String, Object>>().deserialize(responseEntity.getBody());
	}

	public List<HashMap<String, Object>> getListOfMaps(ResponseEntity<String> responseEntity) {
		return new JSONDeserializer<List<HashMap<String, Object>>>().deserialize(responseEntity.getBody());
	}

	public List<String> getList(ResponseEntity<String> responseEntity) {
		return new JSONDeserializer<List<String>>().deserialize(responseEntity.getBody());
	}

}
