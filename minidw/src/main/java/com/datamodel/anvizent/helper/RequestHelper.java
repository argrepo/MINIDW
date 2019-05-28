package com.datamodel.anvizent.helper;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Operations related to HTTP requests.
 * 
 * @author
 *
 */
final public class RequestHelper {
	protected static final Log LOG = LogFactory.getLog(RequestHelper.class);
	private RequestHelper() {}
	
	static public void logAttributes(HttpServletRequest request) {
		Enumeration<String> attrs = request.getAttributeNames();
		StringBuilder out = new StringBuilder("Request Attributes:");
		String attr;
		while(attrs.hasMoreElements()) {
			attr = attrs.nextElement();
			out.append(String.format("\n\t%s = %s", attr, request.getAttribute(attr)));
		}
		LOG.debug(out.toString());
	}
	static public String getContextPath(HttpServletRequest request){
		String resultPath = null;
		return resultPath;
	}
}
