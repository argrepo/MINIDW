package com.anvizent.schedulers.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.anvizent.schedulers.service.UserService;

public class SchedulerServicesAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String tokenHeader = "authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//long startTime = System.currentTimeMillis();
		
		//String authToken = request.getHeader(this.tokenHeader);
		String authToken = request.getParameter(this.tokenHeader);
		//Enumeration<String> headers = request.getHeaderNames();
		/*while (headers.hasMoreElements()) {
			String name = headers.nextElement();
			System.out.println(name+" - " + request.getHeader(name));
		}*/
        if (StringUtils.isBlank(authToken) ) {
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unautherized access");
        	return false;
        }
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        logger.info("checking authentication for user " + username);
        
		if (username != null) {
			logger.info("session is null");
			
			JwtUser userDetails = userService.loadUserByUsername(username);
			
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            	authToken = jwtTokenUtil.refreshToken(authToken);
            	response.setHeader(tokenHeader, "Anthari");
            	//response.setHeader("x-time-zone", request.getParameter("x-time-zone"));
            } else {
            	if (isAjaxRequest(request)) {
    				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired");
    			} else {
    				String baseUrl = StringUtils.substring(request.getRequestURL().toString(), 0,
    						org.apache.commons.lang.StringUtils.ordinalIndexOf(request.getRequestURL().toString(), "/", 4));
    				response.sendRedirect(baseUrl + "/login");
    				return false;
    			}
            }

		} else {
			response.sendError(HttpServletResponse.SC_GONE, "Session expired");
			return false;
		}

		//System.out.println("user checking	 time -- > " + (System.currentTimeMillis() - startTime));
		return true;
	}
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	String authToken = request.getParameter(this.tokenHeader);
    	response.setHeader("myNewHeader", "authToken");
    	//System.out.println(authToken);
    }
	    public boolean isAjaxRequest(HttpServletRequest httpRequest) {
			//System.out.println("x-requested-with :: " + httpRequest.getHeader("x-requested-with"));
			return "XMLHttpRequest".equals(httpRequest.getHeader("x-requested-with"));
		}

}