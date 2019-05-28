package com.datamodel.anvizent.spring;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 
 * web.xml replacement
 * 
 * @author rakesh.gajula
 *
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	protected final Log log = LogFactory.getLog(WebInitializer.class);

	public WebInitializer() {
		log.debug("Web Initializer loaded.");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class, DataSourceConfig.class, SecurityConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return new Filter[] { characterEncodingFilter };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// to avoid loading WebConfig class twice
		return new Class<?>[] {};
	}

	@Override
	public void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}
}
