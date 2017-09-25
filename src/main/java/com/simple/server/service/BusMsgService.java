package com.simple.server.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.IContract;
import com.simple.server.domain.contract.RedirectRouting;
import com.simple.server.http.HttpImpl;
import com.simple.server.util.ObjectConverter;

@Service("busMsgService")
@Scope("singleton")
public class BusMsgService  implements IMsgService{
	
	final private static String LOG_HEADER_NAME = "clazz";	
	
	@Autowired
	private AppConfig appConfig;
	
	@Override
	public void send(MessageChannel msgChannel, IContract msg) throws Exception {
		
		if (msgChannel==null || msg==null)
			return;
		
		msg.setServiceOutDatetime(new Date().toString());
		msg.setServiceIdFrom(appConfig.getServiceId());		
		String json = ObjectConverter.objectToJson(msg);				
		msgChannel.send(MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msg.getClass().getSimpleName()).build());
	}

	@Override
	public void send(MessageChannel msgChannel, String msgHeaderVal, IContract msg) throws Exception {		
		
		if (msgChannel==null || msg==null)
			return;
		
		msg.setServiceOutDatetime(new Date().toString());
		msg.setServiceIdFrom(appConfig.getServiceId());			
		String json = ObjectConverter.objectToJson(msg);				
		msgChannel.send(MessageBuilder.withPayload( json ).setHeader(LOG_HEADER_NAME, msgHeaderVal).build());						
	}
	
	
	@Override
	public void pushQueue(MessageChannel msgChannel, IContract msg) throws Exception {
		
		if (msgChannel==null || msg==null)
			return;
		
		msgChannel.send(MessageBuilder.withPayload( msg ).build());		
	}
	
	@Override
	public @ResponseBody ResponseEntity<String> retranslate(String key, String params){
		
		ResponseEntity<String> res = null;
		try {
			if (appConfig.getRedirectRoutingsHashMap().containsKey(key)) {								
				RedirectRouting redirect = appConfig.getRedirectRoutingsHashMap().get(key);				
				res = HttpImpl.get(redirect, params);							
			}
		} catch (HttpStatusCodeException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getResponseBodyAsString(), HttpImpl.createHeaders(), e.getStatusCode());
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpImpl.createHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}
	
}
