package com.datamodel.anvizent.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.datamodel.anvizent.helper.minidw.Constants;

/**
 * 
 * load all the .config files
 * 
 * @author rakesh.gajula
 *
 */

@Configuration
public class AppProperties {

	static final Logger LOGGER = Logger.getLogger(AppProperties.class);

	private static String minidwServiceConfig = null;

	public AppProperties() {
		LOGGER.info("AppProperties loaded.");
	}

	public static String getMinidwServiceConfig() {
		return minidwServiceConfig;
	}

	public static void setMinidwServiceConfig(String minidwServiceConfig) {
		AppProperties.minidwServiceConfig = minidwServiceConfig;
	}

	/**
	 * Load DB properties from configuration files.
	 * 
	 * @return
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource anvizentDataModelServiceConfig = new FileSystemResource(minidwServiceConfig);
		ppc.setLocations(new Resource[] { anvizentDataModelServiceConfig });
		return ppc;
	}

	static {
		minidwServiceConfig = Constants.Config.MINIDW_SERVICE_CONFIG;
	}
}
