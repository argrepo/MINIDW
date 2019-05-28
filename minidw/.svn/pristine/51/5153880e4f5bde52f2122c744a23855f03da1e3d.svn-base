package com.datamodel.anvizent.spring;

import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.datamodel.anvizent.filter.CrossScriptingFilter;

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
		
		/*Resource packageProps = new ClassPathResource("/messages/package.properties");
		try {
			System.out.println("path --> "+packageProps.getFile().getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		log.debug("Web Initializer loaded.");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class, SecurityConfig.class };
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
		return new Filter[] { characterEncodingFilter, new CrossScriptingFilter() };

	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {};
	}

	@Override
	public void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("defaultHtmlEscape", "true");
		HashSet<SessionTrackingMode> set = new HashSet<SessionTrackingMode>();
        set.add(SessionTrackingMode.COOKIE);
        servletContext.setSessionTrackingModes(set);
	}
}
