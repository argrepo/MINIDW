package com.datamodel.anvizent.filter;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class CookieHandler implements org.springframework.web.servlet.HandlerInterceptor  {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        Cookie cookie = new Cookie("timestamp", new Long(new Date().getTime()).toString());
        cookie.setSecure(httpServletRequest.isSecure());
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        return true;
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}