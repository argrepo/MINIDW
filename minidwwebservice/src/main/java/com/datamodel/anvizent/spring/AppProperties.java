package com.datamodel.anvizent.spring;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.datamodel.anvizent.helper.Constants;

/**
 * 
 * load all the .config files
 * 
 * @author rakesh.gajula
 *
 */

@Configuration
public class AppProperties {

	static final Logger logger = Logger.getLogger(AppProperties.class);

	private static String externalconfig = null;

	public AppProperties() {
		logger.info("AppProperties loaded.");
	}

	/**
	 * Load DB properties from configuration files.
	 * 
	 * @return
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		externalconfig = new File(Constants.Config.EXTERNAL_CONFIG).getAbsolutePath();
		
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource druidconfig = new FileSystemResource(externalconfig);
		ppc.setLocations(new Resource[] {  druidconfig});
		return ppc;
	}
}
