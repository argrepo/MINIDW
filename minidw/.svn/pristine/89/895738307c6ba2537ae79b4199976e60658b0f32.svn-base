package com.datamodel.anvizent.interceptor;

import java.io.IOException;

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
public class WebServiceInterceptor extends HandlerInterceptorAdapter {
	private static final Log LOG = LogFactory.getLog(WebServiceInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//long startTime = System.currentTimeMillis();
		User user = CommonUtils.getUserDetails(request, null, null);


		if (user == null) {
			LOG.info("session is null");

			if (isAjaxRequest(request)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired");
			} else {
				String baseUrl = org.apache.commons.lang.StringUtils.substring(request.getRequestURL().toString(), 0,
						org.apache.commons.lang.StringUtils.ordinalIndexOf(request.getRequestURL().toString(), "/", 4));
				response.sendRedirect(baseUrl + "/login");
			}
			return false;

		} else {
			boolean isValidUser = checkCookie(request, user.getUserId());
			
			if ( !isValidUser ) {
				getCustomResponseMessage(response, HttpServletResponse.SC_FORBIDDEN, "text/html", 
						"<html><body><p>Invalid user access</p></body></html>");
				return false;
			}
		}

		return true;
	}

	public static boolean isAjaxRequest(HttpServletRequest httpRequest) {
		System.out.println("x-requested-with :: " + httpRequest.getHeader("x-requested-with"));
		return "XMLHttpRequest".equals(httpRequest.getHeader("x-requested-with"));
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
	private void getCustomResponseMessage(HttpServletResponse response,int statusCode, String contentType, String body) throws IOException{
		response.setStatus(statusCode);
		response.setContentType(contentType);
		response.getWriter().println(body);
	}
}
