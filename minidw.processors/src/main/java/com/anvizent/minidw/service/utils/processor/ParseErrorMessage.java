package com.anvizent.minidw.service.utils.processor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ErrorLog;

@Component
public class ParseErrorMessage {
	protected static final Log log = LogFactory.getLog(ParseErrorMessage.class);
	
	public String getErrorMessageString(Throwable cause) {
		String errorMessage = null;
		errorMessage = getRootCause(cause).getMessage();
		if (errorMessage == null) {
			errorMessage = "Unable to trace the error";
		}
		return errorMessage;
	}
	
	public  Throwable getRootCause(Throwable cause) {
		Throwable rootCause = null;
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}
	
	public static ErrorLog createErrorLog(Throwable ex, HttpServletRequest request) {
		ErrorLog errorLog = new ErrorLog();
		StringBuilder receivedVariables = new StringBuilder();
		StringBuilder clientInformation = new StringBuilder();
		String userId = "0";
		if (request != null) {

			@SuppressWarnings("unchecked")
			Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			receivedVariables.append("PathVariables : ").append(pathVariables.toString());
			receivedVariables.append("\nRequestParams : ").append(formatReceivedParams(request.getParameterMap()));
			clientInformation.append(request.getHeader(Constants.Config.BROWSER_DETAILS));
			userId = (String) pathVariables.get("clientId");
		}

		StringWriter errorBody = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorBody));

		String errorCode = ex.getLocalizedMessage();
		/*
		 * to handle Null pointer cases; error code will be empty for
		 * NullpointerException
		 */
		if (StringUtils.isBlank(errorCode)) {
			try {
				if (errorBody.toString().contains(";") && errorBody.toString().length() < 500) {
					errorCode = errorBody.toString().substring(0, errorBody.toString().indexOf(";"));
				} else {
					errorCode = errorBody.toString().substring(0, errorBody.toString().indexOf("\n"));
				}
			} catch (Exception e) {
			}
		}
		errorLog.setErrorCode(errorCode);
		errorLog.setErrorBody(errorBody.toString());
		errorLog.setReceivedParameters(receivedVariables.toString());
		errorLog.setUserId(userId);
		errorLog.setClientDetails(clientInformation.toString());

		return errorLog;
	}

	public static String formatReceivedParams(Map<String, String[]> maps) {
		if (maps == null)
			return "";

		StringBuilder paramsMapping = new StringBuilder();
		try {
			String separator = ",";
			maps.forEach((key, value) -> {
				paramsMapping.append(key).append(" = {");
				for (String val : value) {
					paramsMapping.append(val).append(separator);
				}
				paramsMapping.append(" }, ");
			});

		} catch (Exception e) {
			log.error("", e);
		}

		return paramsMapping.toString();
	}

}
