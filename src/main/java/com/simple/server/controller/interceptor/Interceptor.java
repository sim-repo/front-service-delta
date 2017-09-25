package com.simple.server.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.simple.server.config.AppConfig;

@Component
public class Interceptor extends HandlerInterceptorAdapter  {
	
	@Autowired
	private AppConfig appConfig;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {		
		//appConfig.getLogBusMsgService().sendHttpRequest(appConfig.getChannelBusLog(), request);  					
		return super.preHandle(request, response, handler);
	}	
}
