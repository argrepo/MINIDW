package com.datamodel.anvizent.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * Application security initialization.
 * 
 * @author
 *
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		// CSRF for multipart form data filter:
		FilterRegistration.Dynamic springMultipartFilter;
		springMultipartFilter = servletContext.addFilter("springMultipartFilter", new MultipartFilter());
		springMultipartFilter.addMappingForUrlPatterns(null, false, "/*");
	}
}
