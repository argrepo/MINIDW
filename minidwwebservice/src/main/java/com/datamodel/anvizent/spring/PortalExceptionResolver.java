package com.datamodel.anvizent.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.datamodel.anvizent.helper.RequestHelper;
import com.datamodel.anvizent.helper.SessionHelper;

/**
 * Handle uncaught exceptions.
 * 
 * @author
 *
 */
public class PortalExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected String buildLogMessage(Exception ex, HttpServletRequest request) {
		RequestHelper.logAttributes(request);
		SessionHelper.logAttributes(request);
		return super.buildLogMessage(ex, request);
	}
}
