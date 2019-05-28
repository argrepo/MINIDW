package com.anvizent.minidw.client.jdbc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommonUtils {
 
	public static String getConnectionUrl(String serverIPAndPort, String protocal) {
		JSONObject jsonObject = new JSONObject(serverIPAndPort);
		JSONArray sourcemappingArray = jsonObject.getJSONArray("connectionParams");
		JSONObject sources = null;
		for (int i = 0; i < sourcemappingArray.length(); i++) {
			sources = sourcemappingArray.getJSONObject(i);
			protocal = StringUtils.replace(protocal, "{" + sources.getString("placeholderKey") + "}",
					sources.getString("placeholderValue"));
		}
		String queryParam = jsonObject.getString("queryParams");
		if(StringUtils.isNotBlank(queryParam)) {
			protocal += queryParam.trim();
		}
		
		return protocal;
	}

	public static String removePlaceHoldersIfAny(String protocal) {
		if (StringUtils.isBlank(protocal)) {
			return protocal;
		}
		int index = protocal.indexOf("{");
		if (index != -1) {
			protocal = StringUtils.substring(protocal, 0, index);
		}
		return protocal;
	}
	public static String getFormatedQuery(String ilQuery,JSONArray jsonDbVar) {
		String formatQuery = ilQuery;
			JSONObject dbVariables =null;
			if(jsonDbVar != null && jsonDbVar.length() > 0) {
				for(int i=0; i < jsonDbVar.length();i++) {
					dbVariables = jsonDbVar.getJSONObject(i);
					formatQuery = StringUtils.replace(ilQuery,  dbVariables.getString("key") , dbVariables.getString("value"));
					ilQuery = formatQuery;
				}
			}
			return formatQuery;
		}
	public static String currentTime() {
		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = localDateFormat.format(new Date());
		return time.replace(":", "");
		
	}
}