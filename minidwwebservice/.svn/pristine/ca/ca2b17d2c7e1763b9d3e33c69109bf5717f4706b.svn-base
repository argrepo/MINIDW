package com.datamodel.anvizent.AmazonS3;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import sun.misc.BASE64Encoder;

public class JersyGetClient {
	  public static void main(String a[]){
	         
	        String url = "https://www.eshbelsaas.com/demo/odata/Priority/tabDEMheb.ini/apidev/CUSTOMERS";
	        String name = "lakip";
	        String password = "Lp123+";
	        String authString = name + ":" + password;
	        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
	        System.out.println("Base64 encoded auth string: " + authStringEnc);
	        
	       /* Client restClient = Client.create();
	        WebResource webResource = restClient.resource(url);
	        ClientResponse resp = webResource.accept("application/json")
	                                         .header("Authorization", "Basic " + authStringEnc)
	                                         .get(ClientResponse.class);
	        if(resp.getStatus() != 200){
	            System.err.println("Unable to connect to the server");
	        }
	        String output = resp.getEntity(String.class);
	        System.out.println("response: "+output);*/
	        RestTemplate rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			headers.add("Authorization", "Basic "+authStringEnc);
			headers.add("Content-Type", "application/json");
			headers.add("Accept", "application/json");
			HttpEntity<Object> tsr = new HttpEntity<Object>(new LinkedMultiValueMap<>(), headers);
			
			ResponseEntity<Map> map = rt.exchange(url, HttpMethod.GET, tsr, Map.class);
			System.out.println("map " + map);
	        
	        
	    }
}

