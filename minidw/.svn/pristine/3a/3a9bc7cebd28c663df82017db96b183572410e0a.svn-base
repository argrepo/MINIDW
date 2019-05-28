/**
 * 
 */
package com.datamodel.anvizent.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author rakesh.gajula
 *
 */
public class MinidwAuthenticationInterceptor extends HandlerInterceptorAdapter {

	static final Logger LOGGER = Logger.getLogger(MinidwAuthenticationInterceptor.class);

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		boolean isAuthenticated = Boolean.FALSE;
		Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables != null && !pathVariables.isEmpty()) {
			isAuthenticated = true;
		}
		if (!isAuthenticated) {
			response.getOutputStream().write(serialize("Client id is not valid"));
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return isAuthenticated;
	}

	private byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		byte[] array = out.toByteArray();
		out.close();
		os.close();
		return array;
	}
}
