package com.datamodel.anvizent.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import com.datamodel.anvizent.spring.AppProperties;

@Import(AppProperties.class)
public class CustomAccessDeniedHandler  extends AccessDeniedHandlerImpl {
	private static Logger LOGGER = Logger.getLogger(CustomAccessDeniedHandler.class);
	  @Override
	    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
	        if(accessDeniedException instanceof MissingCsrfTokenException || accessDeniedException instanceof InvalidCsrfTokenException) {
	           throw new ServletException(accessDeniedException);
	        }
	        super.handle(request, response, accessDeniedException);
	         
	    }
	  
}
