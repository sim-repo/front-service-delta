package com.simple.server.service;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.simple.server.domain.contract.IContract;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.util.ObjectConverter;

@Service("busMsgService")
@Scope("singleton")
public class BusMsgService  implements IMsgService{
	
	final private static String LOG_HEADER_NAME = "clazz";
	
	final private static String SERVICE_ID = "front"; 
	
	@Override
	public void send(MessageChannel msgChannel, IContract msg) throws Exception {
		
		if (msgChannel==null || msg==null)
			return;
		
		msg.setServiceOutDatetime(new Date().toString());
		msg.setServiceIdFrom(SERVICE_ID);		
		String json = ObjectConverter.ObjectToJson(msg);		
		System.out.println("front::::: "+json);
		msgChannel.send(MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msg.getClass().getSimpleName()).build());
	}

	@Override
	public void send(MessageChannel msgChannel, String msgHeaderVal, IContract msg) throws Exception {		
		
		if (msgChannel==null || msg==null)
			return;
		
		msg.setServiceOutDatetime(new Date().toString());
		msg.setServiceIdFrom(SERVICE_ID);			
		String json = ObjectConverter.ObjectToJson(msg);				
		System.out.println("front:::::  "+json);
		msgChannel.send(MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msgHeaderVal).build());						
	}
	
	
	@Override
	public void pushQueue(MessageChannel msgChannel, IContract msg) throws Exception {
		
		if (msgChannel==null || msg==null)
			return;
		
		msgChannel.send(MessageBuilder.withPayload( msg ).build());		
	}
	
	
}
