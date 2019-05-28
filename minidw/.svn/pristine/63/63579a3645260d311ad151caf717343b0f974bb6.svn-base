package com.datamodel.anvizent.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * Generate the object model for site navigation.
 * 
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
public class NavigationInterceptor extends HandlerInterceptorAdapter {
	private static final Log LOG = LogFactory.getLog(NavigationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		User user = CommonUtils.getUserDetails(request, null, null);
		
		String baseUrl = "";
		if (request.getContextPath() != null && request.getContextPath().toString().equals("")) {
			baseUrl = "/";
		} else {
			baseUrl = request.getContextPath();
		}
		baseUrl = StringUtils.substring(request.getRequestURL().toString(), 0, StringUtils.ordinalIndexOf(request.getRequestURL().toString(), "/", 4));

		if (user != null) {
			boolean isValidUser = checkCookie(request, user.getUserId());
			
			if ( !isValidUser ) {
				getCustomResponseMessage(response, HttpServletResponse.SC_FORBIDDEN, "text/html", 
						"<html><body><p>Invalid user access</p></body></html>");
				return false;
			}
			
			List<String> keywords = new ArrayList<>();
			keywords.add("query");
			keywords.add("sql");

			if (isKeywordfound(request, keywords)) {
				getCustomResponseMessage(response, HttpServletResponse.SC_FORBIDDEN, "text/html", "<html><body><p>Any of <span style='color:red;font-weight:bold;'>" + keywords
						+ "</span> parameters found in the request</p></body></html>");
				return false;
			}
			
			boolean isValidAccess = true;
			String accessedLink = request.getRequestURI();
			accessedLink = accessedLink.substring(accessedLink.indexOf("/", 3), accessedLink.length());
			if (accessedLink.startsWith("/adt") || accessedLink.startsWith("/adv")) {
				if (accessedLink.contains("s3AuditLogs")) {

				} else if ((user.getRoleId() == Constants.Role.SUPERADMIN && user.getRoleId() == Constants.Role.SUPERADMIN_ETLADMIN)  || user.getRoleId() == 1) {
					isValidAccess = false;
				} 
			} else if (accessedLink.startsWith("/admin")) {

				if ((user.getRoleId() != Constants.Role.SUPERADMIN && user.getRoleId() != Constants.Role.SUPERADMIN_ETLADMIN) && user.getRoleId() != 1) {
					isValidAccess = false;
				}
			}
			
			if (!isValidAccess) {
				response.sendRedirect(baseUrl + "/accessDenied");
				return false;
			}

		} else {
			LOG.info("session is null");

			response.sendRedirect(baseUrl + "/sessionTimeOut");
			return false;
		}

		return true;
	}

	private void getCustomResponseMessage(HttpServletResponse response,int statusCode, String contentType, String body) throws IOException{
		response.setStatus(statusCode);
		response.setContentType(contentType);
		response.getWriter().println(body);
	}
	private boolean checkCookie(HttpServletRequest request, String userId) throws Exception {
		Cookie[] cookies = request.getCookies();
		String userKey = Constants.Config.USER_COOKIE_NAME+StringUtils.replace(StringUtils.replace(EncryptionServiceImpl.getInstance().encrypt(userId).toUpperCase(), "_", ""), "-", "");
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(userKey)) {
				String cookieValue = cookie.getValue();
				if (StringUtils.isNotBlank(cookieValue)) {
					cookieValue = EncryptionServiceImpl.getInstance().decrypt(cookieValue);
					String[] userAndTimeValue = StringUtils.split(cookieValue,"#");
					if ( userAndTimeValue.length  == 3 && userAndTimeValue[0].equals(userId) && userAndTimeValue[2].equals(userId) ) {
						return true;
					}
				}
				break;
			}
		}
		System.out.println("Invalid User");
		return false;
	}

	private boolean isKeywordfound(HttpServletRequest request, List<String> keyWords) {
		Enumeration<String> parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String key = parameters.nextElement().toLowerCase();
			if (keyWords.contains(key)) {
				return true;
			}
		}
		return false;
	}

}
