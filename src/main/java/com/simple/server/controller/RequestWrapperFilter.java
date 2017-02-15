package com.simple.server.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class RequestWrapperFilter implements Filter {
	 public void init(FilterConfig config)
	      throws ServletException{
	 // nothing goes here
	 }
	 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
		
		MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest)request);
	
	     chain.doFilter(requestWrapper,response);
	}

	public void destroy( ){
	
	}
}