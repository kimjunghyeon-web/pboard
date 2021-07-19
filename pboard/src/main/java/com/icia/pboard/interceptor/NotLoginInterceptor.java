package com.icia.pboard.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class NotLoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication.getPrincipal().toString().equals("anonymousUser")==false ) {
			throw new IllegalAccessException("잘못된 접근입니다.");
		}
		
		return true;
	}


}
