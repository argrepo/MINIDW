package com.datamodel.anvizent.spring;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Root configuration.
 * 
 * define service impl, dao impl beans and other business related beans
 * 
 * @author rakesh.gajula
 * 
 */
@Configuration
@Import(AppProperties.class)
@ComponentScan(basePackages = { "com.datamodel.anvizent" })
@EnableWebMvc
public class RootConfig {
	protected final Log log = LogFactory.getLog(RootConfig.class);
	private @Value("${service.contextPath:}") String serviceContextPath;

	public RootConfig() {
		log.debug("Root Configuration loaded.");
	}

	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Bean
	public PropertiesFactoryBean propertiesFactoryBean() {
		PropertiesFactoryBean pfb = new PropertiesFactoryBean();
		Properties prop = new Properties();
		prop.setProperty("service.contextPath", serviceContextPath);
		pfb.setProperties(prop);
		return pfb;
	}

}
