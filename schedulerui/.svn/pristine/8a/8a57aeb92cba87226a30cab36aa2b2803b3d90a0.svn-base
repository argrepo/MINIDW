package com.anvizent.schedulers.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.anvizent.schedulers.service.UserService;

public class JwtAuthenticationTokenFilter extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String tokenHeader = "authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		
		String authToken = request.getHeader(this.tokenHeader);
        if (authToken.startsWith("Bearer ")) {
        	authToken = authToken.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        logger.info("checking authentication für user " + username);
        
		if (username != null) {
			logger.info("session is null");
			
			JwtUser userDetails = userService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				
			}
			
			// It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            	authToken = jwtTokenUtil.refreshToken(authToken);
            	response.setHeader(tokenHeader, authToken);
            } else {
            	if (isAjaxRequest(request)) {
    				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired");
    			} else {
    				String baseUrl = org.apache.commons.lang.StringUtils.substring(request.getRequestURL().toString(), 0,
    						org.apache.commons.lang.StringUtils.ordinalIndexOf(request.getRequestURL().toString(), "/", 4));
    				response.sendRedirect(baseUrl + "/login");
    				return false;
    			}
            }

			

		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired");
			return false;
		}

		System.out.println("user checking time -- > " + (System.currentTimeMillis() - startTime));
		return true;
	}
    public boolean isAjaxRequest(HttpServletRequest httpRequest) {
		System.out.println("x-requested-with :: " + httpRequest.getHeader("x-requested-with"));
		return "XMLHttpRequest".equals(httpRequest.getHeader("x-requested-with"));
	}

}