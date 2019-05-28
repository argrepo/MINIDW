package com.anvizent.client.scheduler.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.spring.AppProperties;

public class PropertiesPlaceHolder {

	protected static final Log log = LogFactory.getLog(PropertiesPlaceHolder.class);

	private final Properties properties = new Properties();
	String baseConfigPath = Constants.SchedulerFileNames.getBaseConfigPath();
	public PropertiesPlaceHolder() throws FileNotFoundException, IOException {
		refreshProperties();
	}
	
	public void refreshProperties() throws FileNotFoundException, IOException {
		
		FileReader minidwServiceConfigReader = new FileReader(AppProperties.getMinidwServiceConfig());
		properties.load(minidwServiceConfigReader);
		minidwServiceConfigReader.close();
	}
	
	public String getAccessKey() {
		return properties.getProperty(Constants.Config.ACCESS_KEY);
	}
	
}