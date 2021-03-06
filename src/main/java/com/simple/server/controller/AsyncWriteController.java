package com.simple.server.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.config.OperationType;
import com.simple.server.domain.contract.BusClassificator;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.domain.contract.BusTagTemplate;
import com.simple.server.domain.contract.BusWriteMsg;
import com.simple.server.statistics.time.Timing;
import com.simple.server.util.DateTimeConverter;


@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async/add/")
public class AsyncWriteController {
	
	@Autowired
	private AppConfig appConfig;
		
	@RequestMapping(value = "json/nav/so", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNavSo(@RequestBody SorderMsg so) {	
		try {																	
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			so.setMethodHandler("async/post/json/nav/so");
			so.setChannel(appConfig.getChannelBusBridge());
			so.setLogClass(BusWriteMsg.class);
			so.setEndPointId("NAV_WORK");
			so.setOperationType(OperationType.WRITE);
			so.setRequestInDatetime(DateTimeConverter.getCurDate());
			so.setJuuid(UUID.randomUUID().toString());			
			appConfig.getQueueDirtyMsg().put(so);	
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	@RequestMapping(value = "text/nav/so", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
	public StatusMsg textNavSo(HttpServletRequest request, HttpServletResponse response) {	
		try {																			
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	@RequestMapping(value = "json/nav/tag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNavTag(@RequestBody BusTagTemplate tag) {	
		try {																									
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			tag.setMethodHandler("async/post/json/nav/tag");
			tag.setChannel(appConfig.getChannelBusLog());
			tag.setLogClass(BusWriteMsg.class);
			tag.setEndPointId(appConfig.LOG_ENDPOINT_NAME);
			tag.setOperationType(OperationType.WRITE);
			tag.setRequestInDatetime(DateTimeConverter.getCurDate());
			tag.setJuuid(UUID.randomUUID().toString());						
			appConfig.getQueueDirtyMsg().put(tag);	
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}	
	
	@RequestMapping(value = "json/classificator", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg home(@RequestBody BusClassificator classificator) {		
		try {	
			
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			classificator.setMethodHandler("async/post/json/classificator");
			classificator.setChannel(appConfig.getChannelBusLog());
			classificator.setLogClass(BusWriteMsg.class);
			classificator.setEndPointId(appConfig.LOG_ENDPOINT_NAME);
			classificator.setOperationType(OperationType.WRITE);
			classificator.setRequestInDatetime(DateTimeConverter.getCurDate());
			classificator.setJuuid(UUID.randomUUID().toString());									
			appConfig.getQueueDirtyMsg().put(classificator);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {	
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}		
	}
	
}
