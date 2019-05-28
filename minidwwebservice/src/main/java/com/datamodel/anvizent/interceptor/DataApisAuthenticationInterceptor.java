/**
 * 
 */
package com.datamodel.anvizent.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author rajesh.anthari
 *
 */
public class DataApisAuthenticationInterceptor extends HandlerInterceptorAdapter {

	static final Logger LOGGER = Logger.getLogger(DataApisAuthenticationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isAuthenticated = Boolean.TRUE;
		String xAuthCodeHdr = request.getHeader("X-Authentication-code");
		String xAuthCodeReq = request.getParameter("X-Authentication-code");
		if (xAuthCodeHdr == null && xAuthCodeReq == null) {
			getUnAuthorisedResponse(response);
			return false;
		}

		return isAuthenticated;
	}

	private void getUnAuthorisedResponse(HttpServletResponse response) throws Exception {
		response.getOutputStream().write("Invalid access".getBytes());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
