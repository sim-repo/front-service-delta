package com.simple.server.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.config.EndpointType;
import com.simple.server.config.EventType;
import com.simple.server.config.OperationType;
import com.simple.server.domain.contract.BusPubMsg;
import com.simple.server.domain.contract.BusReadMsg;
import com.simple.server.domain.contract.BusWriteMsg;
import com.simple.server.domain.contract.CustMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.statistics.time.Timing;

@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async")
public class AsyncPubController {
	
	@Autowired
	private AppConfig appConfig;	
	
	
	@RequestMapping(value = "json/sub/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonConfirm(@RequestBody StatusMsg status) {		
		try {	
			
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			
			status.setMethodHandler("/async/json/sub/confirm");			
			
			status.setChannel(appConfig.getChannelBusBridge());
			//status.setEventId(EventType.CHANGE_CUST);
			status.setLogClass(BusPubMsg.class);
			status.setOperationType(OperationType.SUB);			
							
			appConfig.getQueueDirtyMsg().put(status);
			
			
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
		
	
	//*only for testing
	//**	
	@RequestMapping(value = "json/pub/cust", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonCustPub(@RequestBody CustMsg cust) {		
		try {																					
					
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			cust.setMethodHandler("/async/pub/json/cust");			
			cust.setChannel(appConfig.getChannelBusBridge());
			cust.setEventId(EventType.CHANGE_CUST);
			cust.setLogClass(BusPubMsg.class);
			cust.setOperationType(OperationType.PUB);			
							
			appConfig.getQueueDirtyMsg().put(cust);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav1/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav1Listener(@RequestBody CustMsg cust) {		
		try {	
			
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("client catch ");
			cust.setMethodHandler("nav1/listener");			
			cust.setChannel(appConfig.getChannelBusBridge());
			cust.setLogClass(BusWriteMsg.class);			
			cust.setOperationType(OperationType.WRITE);
			cust.setEndPointId(EndpointType.NAV);
			
			appConfig.getQueueDirtyMsg().put(cust);				
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav2/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav2Listener(@RequestBody CustMsg cust) {		
		try {	
			
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("client catch ");
			cust.setMethodHandler("nav2/listener");			
			cust.setChannel(appConfig.getChannelBusBridge());
			cust.setLogClass(BusWriteMsg.class);			
			cust.setOperationType(OperationType.WRITE);
			cust.setEndPointId(EndpointType.ONE);
			
			appConfig.getQueueDirtyMsg().put(cust);				
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	
	@RequestMapping(value = "publisher/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonPubListener(@RequestBody StatusMsg status) {		
		try {																					
			
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("publisher/listener catch !!!!!!!!!!!!!!! ");
			
			status.setMethodHandler("publisher/listener");			
			status.setChannel(appConfig.getChannelBusBridge());
			status.setLogClass(BusWriteMsg.class);			
			status.setOperationType(OperationType.WRITE);
			status.setEndPointId(EndpointType.NAV);			
							
			//appConfig.getQueueDirtyMsg().put(status);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	
}
