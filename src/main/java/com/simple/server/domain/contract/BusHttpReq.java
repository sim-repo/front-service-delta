package com.simple.server.domain.contract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import com.simple.server.config.AppConfig;

public class BusHttpReq extends AContract{
	
	String contextPath;
	String contentType;
	String localAddr;
	String localName;
	String method;
	String pathInfo;
	String protocol;
	String queryString;
	String remoteAddr;
	String remoteHost;
	String remotePort;
	String remoteUser;
	String serverName;
	String serverPort;
	String requestURL;	
	
	
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
			
	public void copyFrom(HttpServletRequest httpRequest) {
		
		 this.contextPath	=	httpRequest.getContextPath(); 
		 this.contentType	=	httpRequest.getContentType();
		 this.localAddr	=	httpRequest.getLocalAddr();
		 this.localName	=	httpRequest.getLocalName();
		 this.method	=	httpRequest.getMethod();
		 this.pathInfo	=	httpRequest.getPathInfo();
		 this.protocol	=	httpRequest.getProtocol();
		 this.queryString	=	httpRequest.getQueryString();
		 this.remoteAddr	=	httpRequest.getRemoteAddr();
		 this.remoteHost	=	httpRequest.getRemoteHost();
		 this.remotePort	=	httpRequest.getRemotePort()+"";
		 this.remoteUser	=	httpRequest.getRemoteUser();		 
		 this.serverName	=	httpRequest.getServerName();
		 this.serverPort	=	httpRequest.getServerPort()+"";		 
		 this.requestURL	=	httpRequest.getRequestURI();
		 this.requestInDatetime = new SimpleDateFormat(AppConfig.DATEFORMAT).format(Calendar.getInstance().getTime());
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPathInfo() {
		return pathInfo;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
	
}
