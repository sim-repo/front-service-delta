package com.simple.server.handler;

public interface MsgHandler {
	void handleJsonMessage(String json) throws Exception;
}
