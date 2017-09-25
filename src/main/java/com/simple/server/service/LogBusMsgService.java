package com.simple.server.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.AContract;
import com.simple.server.domain.contract.ALogContract;
import com.simple.server.domain.contract.BusHttpReq;
import com.simple.server.domain.contract.IContract;
import com.simple.server.util.DateTimeConverter;
import com.simple.server.util.ObjectConverter;


@Service("logBusMsgService")
@Scope("singleton")
public class LogBusMsgService{
	
	
	final private static String LOG_HEADER_NAME = "log"; 
	
	@Autowired
	private AppConfig appConfig;
	
	public void transformAndSend(MessageChannel msgChannel, AContract msg) throws Exception {		
		IContract message = newLogMsg(msg);												
		String json = ObjectConverter.objectToJson(message);
		msgChannel.send( MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msg.getClass().getSimpleName()).build() );					
	}

	
	public void sendHttpRequest(MessageChannel msgChannel, HttpServletRequest request) throws Exception {			
		BusHttpReq logHttpReq = new BusHttpReq();
		logHttpReq.copyFrom(request);
		logHttpReq.setJuuid(UUID.randomUUID().toString());						
		String json = ObjectConverter.objectToJson(logHttpReq);		
		msgChannel.send( MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, logHttpReq.getClass().getSimpleName()).build() );					
	}
	
	
	public void sendAsIs(MessageChannel msgChannel, IContract msg) throws Exception {			
		msg.setJuuid(UUID.randomUUID().toString());						
		String json = ObjectConverter.objectToJson(msg);		
		msgChannel.send( MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msg.getClass().getSimpleName()).build() );					
	}
	
	
	public ALogContract newLogMsg(AContract msg) throws Exception{		
		
		ALogContract instance = msg.getLogClass().newInstance();		
		instance.setServiceOutDatetime(DateTimeConverter.getCurDate());		
		instance.setJuuid(msg.getJuuid());
		instance.setEndPointId(msg.getEndPointId());
		instance.setSenderId(msg.getSenderId());
		instance.setEventId(msg.getEventId());
		instance.setResponseURI(msg.getResponseURI());
		instance.setResponseContentType(msg.getResponseContentType());
		instance.setResponseContractClass(msg.getResponseContractClass());		
		instance.setMethodHandler(msg.getMethodHandler());
		instance.setServiceIdFrom(appConfig.getServiceId());			
		instance.setMessageHeaderValue(msg.getClass().getSimpleName());
		//instance.setMessageBodyValue(msg.toString());				
		return instance;
	}

}
