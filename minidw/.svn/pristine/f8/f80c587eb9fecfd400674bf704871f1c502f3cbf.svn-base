package com.datamodel.anvizent.helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CheckServerURLStatus {

	@SuppressWarnings("rawtypes")
	public static Map checkConnectionUrls(String endPointUrls) throws IOException {
		Map<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
		System.out.println("endPointUrls------" + endPointUrls);
		String[] connectionURLS = endPointUrls.split(",");
		for (String URL : connectionURLS) {
			HashMap<String, String> status = checkURlStatus(URL);
			result.put(URL, status);
			System.out.println("URL " + URL + " status " + status);

		}
		return result;
	}

	public static  HashMap<String, String> checkURlStatus(String url) {
		HashMap<String, String> map = new HashMap<>();
		boolean isUrlReachable = false;
		try {
			if (isUrlReachable(url)) {
				isUrlReachable = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			map.put("msg", e.getLocalizedMessage());
			System.out.println("e.getLocalizedMessage()==============="+e.getLocalizedMessage());
			System.out.println("getMessage==========="+e.getMessage());
		}
		map.put("status", Boolean.toString(isUrlReachable));
		return map;
	}

	public static boolean isUrlReachable(String sUrl) throws IOException {
		boolean isReachble = false;
		HttpURLConnection httpURLConnection = null;

		httpURLConnection = (HttpURLConnection) new URL(sUrl).openConnection();
		httpURLConnection.setRequestMethod("HEAD");
		httpURLConnection.connect();
		isReachble = (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
		return isReachble;
	}

}
