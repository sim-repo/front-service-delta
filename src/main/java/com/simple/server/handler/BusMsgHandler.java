package com.simple.server.handler;

import org.springframework.stereotype.Service;

@Service("busMsgHandler")
public class BusMsgHandler implements MsgHandler{

	@Override
	public void handleJsonMessage(String json) throws Exception {
		
	}
	
}
