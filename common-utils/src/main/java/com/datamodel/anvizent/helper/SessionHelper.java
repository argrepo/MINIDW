package com.datamodel.anvizent.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper functions for session interaction.
 * 
 * @author Hari Anasuri
 *
 */
final public class SessionHelper {
	protected static final Log LOG = LogFactory.getLog(RequestHelper.class);

	private SessionHelper() {
	}

	/**
	 * Move a session attribute from the session to the request.
	 * 
	 * @param request
	 * @param attrName
	 */
	static public void moveToRequest(HttpServletRequest request, String attrName) {
		if (request == null || request.getSession() == null)
			return;
		HttpSession session = request.getSession();
		request.setAttribute(attrName, session.getAttribute(attrName));
		session.removeAttribute(attrName);
	}

	/**
	 * Log all the session attributes for debugging purposes.
	 * 
	 * @param request
	 */
	static public void logAttributes(HttpServletRequest request) {
		if (request == null || request.getSession() == null)
			return;
		logAttributes(request.getSession());
	}

	/**
	 * Log all the session attributes for debugging purposes.
	 * 
	 * @param session
	 */
	static public void logAttributes(HttpSession session) {
		Enumeration<String> attrs = session.getAttributeNames();
		StringBuilder out = new StringBuilder("Session Attributes:");
		String attr;
		while (attrs.hasMoreElements()) {
			attr = attrs.nextElement();
			out.append(String.format("\n\t%s = %s", attr, session.getAttribute(attr)));
		}
		LOG.debug(out.toString());
	}

	static public void setSesionAttribute(HttpServletRequest request, String attributeName, Object sessionObject) {
		HttpSession session = request.getSession(false);

		if (session != null)
			session.setAttribute(attributeName, sessionObject);
	}

	static public Object getSesionAttribute(HttpServletRequest request, String attributeName) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;
		return session.getAttribute(attributeName);
	}

	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	public static String getIpAddress() {
		String ipaddress = "";
		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getLocalHost();
			ipaddress = ipAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipaddress;

	}
}
