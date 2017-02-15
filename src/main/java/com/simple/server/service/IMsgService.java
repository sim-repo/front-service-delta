package com.simple.server.service;

import org.springframework.messaging.MessageChannel;

import com.simple.server.domain.contract.IContract;
import com.simple.server.domain.contract.Status;


public interface IMsgService {
	public void send(MessageChannel msgChannel, IContract msg) throws Exception;	
	void send(MessageChannel msgChannel, String msgHeaderVal, IContract msg) throws Exception;
	public void pushQueue(MessageChannel msgChannel, IContract msg) throws Exception;
	
}
