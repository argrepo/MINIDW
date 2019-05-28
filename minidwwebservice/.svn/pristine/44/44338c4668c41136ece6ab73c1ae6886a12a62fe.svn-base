package com.datamodel.anvizent.spring;

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
		insertFilters(servletContext, new MultipartFilter());
	}
}
