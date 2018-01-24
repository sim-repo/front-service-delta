package com.simple.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.server.domain.contract.IContract;


public interface IMsgService {
	void send(MessageChannel msgChannel, IContract msg) throws Exception;	
	void send(MessageChannel msgChannel, String msgHeaderVal, IContract msg) throws Exception;
	void pushQueue(MessageChannel msgChannel, IContract msg) throws Exception;
	@ResponseBody ResponseEntity<String> retranslate(String key, String params);
	@ResponseBody String checkRetranslate(String key);
}
