package com.simple.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.simple.server.config.AppConfig;

public abstract class AbstractMsgHandler implements MsgHandler{
	@Autowired
	private AppConfig appConfig;

	public AppConfig getAppConfig(){
		return appConfig;
	}
}
