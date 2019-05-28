/**
 * 
 */
package com.datamodel.anvizent.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rakesh.gajula
 *
 */
public class AnvizentAuthenticationInterceptor extends HandlerInterceptorAdapter {

	static final Logger LOGGER = Logger.getLogger(AnvizentAuthenticationInterceptor.class);

	@Autowired
	private UserDetailsService userService;
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		boolean isAuthenticated = Boolean.TRUE;
		Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables != null && !pathVariables.isEmpty()) {
			String xAuthCode = request.getHeader("X-Authentication-code");
			String userId = (String) pathVariables.get(Constants.PathVariables.CLIENT_ID);

			String plainClientId = request.getHeader(com.datamodel.anvizent.helper.minidw.Constants.Config.HEADER_CLIENT_ID);
			String plainUserClientId = request.getHeader(com.datamodel.anvizent.helper.minidw.Constants.Config.HEADER_USER_CLIENT_ID);
			
			if ( StringUtils.isBlank(plainClientId)) {
				getUnAuthorisedResponse(response);
				return false;
			}
			
			if (xAuthCode != null) {
				String userIdXAuth = EncryptionServiceImpl.getInstance().decrypt(xAuthCode);
				String[] userIdAndLocale = StringUtils.split(userIdXAuth, '#');
				userIdXAuth = userIdAndLocale[0];
				if (!userId.equals(userIdXAuth)) {
					getUnAuthorisedResponse(response);
					return false;
				}
			} else {
				getUnAuthorisedResponse(response);
				return false;
			}

			if (userId != null && userId.length() > 8) {
				userId = EncryptionServiceImpl.getInstance().decrypt(userId);
				String[] userIdAndLocale = StringUtils.split(userId, '#');
				userId = userIdAndLocale[0];

				if (userIdAndLocale.length > 1) {
					String locale = userIdAndLocale[1];
					CookieLocaleResolver resolver = new CookieLocaleResolver();
					resolver.setCookieHttpOnly(true);
					resolver.setCookieSecure(true);
					if (StringUtils.isNotBlank(locale) && !locale.equalsIgnoreCase("null")) {
						if (StringUtils.contains(locale, "_")) {
							String loc[] = locale.split("_");
							
							resolver.setLocale(request, response, new Locale(loc[0], loc[1]));

						} else {
							
							resolver.setLocale(request, response, new Locale(locale));
						}

					}
				}
				pathVariables.put(Constants.PathVariables.CLIENT_ID, userId);
				request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathVariables);
			} else {
				getUnAuthorisedResponse(response);
				return false;
			}
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(plainUserClientId);
			User user = userService.getUserDetailszd(userId, clientJdbcInstance.getClientAppdbJdbcTemplate());
			if (user != null) {
				String accessedLink = request.getRequestURI();
				accessedLink = accessedLink.substring(accessedLink.indexOf("/", 3), accessedLink.length());
				//System.err.println("accessedLink  - > "+accessedLink);
				if (accessedLink.startsWith("/anvizentServices")) {
					if (user.getRoleId() == Constants.Role.SUPERADMIN || user.getRoleId() == Constants.Role.SUPERADMIN_ETLADMIN) {
						isAuthenticated = Boolean.FALSE;
					}
				} else if (accessedLink.startsWith("/adminServices")) {
					if (user.getRoleId() != Constants.Role.SUPERADMIN || user.getRoleId() != Constants.Role.SUPERADMIN_ETLADMIN) {
						isAuthenticated = Boolean.FALSE;
					}
				}

			} else {
				if (userId.equals("-1") || userId.length() > 6) {
					isAuthenticated = Boolean.TRUE;
				} else {
					isAuthenticated = Boolean.FALSE;
				}
			}
		}
		if (!isAuthenticated) {
			response.getOutputStream().write("Client/User id is not valid".getBytes());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return isAuthenticated;
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = request.getReader();

			if (bufferedReader != null) {

				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (Exception ex) {
			System.out.println("" + ex.getMessage());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	private void getUnAuthorisedResponse(HttpServletResponse response) throws Exception {
		response.getOutputStream().write("Invalid access".getBytes());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
