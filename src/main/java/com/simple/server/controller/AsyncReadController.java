package com.simple.server.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.config.EndpointType;
import com.simple.server.config.OperationType;
import com.simple.server.domain.contract.BusReadMsg;
import com.simple.server.domain.contract.BusWriteMsg;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.domain.contract.BusReportMsg;
import com.simple.server.domain.contract.Status;
import com.simple.server.statistics.time.Timing;



@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async/get/")
public class AsyncReadController {

	@Autowired
	private AppConfig appConfig;
	
	
	@RequestMapping(value = "json/nav/so", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Status jsonNavSoGet(@RequestBody SorderMsg so) {		
		try {																					
					
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			so.setMethodHandler("/async/get/json/nav/so");			
			so.setChannel(appConfig.getChannelBusBridge());
			so.setLogClass(BusReadMsg.class);
			so.setJuuid(UUID.randomUUID());			
			appConfig.getQueueDirtyMsg().put(so);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new Status("406", e.toString());
		}
	}
	
	@RequestMapping(value = "json/nav/tag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Status  jsonNavTagGet(@RequestBody BusReportMsg req){				
		try {			

			Thread.currentThread().sleep(Timing.getTimeMaxSleep());		
			req.setMethodHandler("/async/get/json/nav/tag");
			req.setLogClass(BusReadMsg.class);
			req.setEndPointId(EndpointType.NAV);
			req.setOperationType(OperationType.READ);
			req.setChannel(appConfig.getChannelBusBridge());
			req.setRequestInDatetime(new SimpleDateFormat(AppConfig.DATEFORMAT).format(Calendar.getInstance().getTime()));
			req.setJuuid(UUID.randomUUID());						
			appConfig.getQueueDirtyMsg().put(req);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new Status("406", e.toString());
		}		
	}		
}
