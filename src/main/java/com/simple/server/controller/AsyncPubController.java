package com.simple.server.controller;

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
import com.simple.server.domain.contract.BusSubMsg;
import com.simple.server.domain.contract.BusWriteMsg;
import com.simple.server.domain.contract.ErrMsg;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.domain.contract.UniMsg;
import com.simple.server.statistics.time.Timing;

@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async")
public class AsyncPubController {
	
	@Autowired
	private AppConfig appConfig;		
		
	@RequestMapping(value = "json/pub/uni", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonPub(@RequestBody UniMsg msg) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			msg.setMethodHandler("/async/json/pub/uni");						
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusPubMsg.class);
			msg.setOperationType(OperationType.PUB);		
			msg.setEventId(EventType.ANY);
			System.out.println(msg);
			appConfig.getQueueDirtyMsg().put(msg);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav1/uni/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav1Listener2(@RequestBody UniMsg msg) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("nav1 catch ::::: "+msg);
			msg.setMethodHandler("nav1/uni/listener");			
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);			
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.LOG);	
			msg.setIsDirectInsert(true);
		//	appConfig.getQueueDirtyMsg().put(msg);				
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav2/uni/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav2Listener2(@RequestBody UniMsg msg) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("nav2 catch ::::: " +msg);
			msg.setMethodHandler("nav2/uni/listener");			
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusWriteMsg.class);			
			msg.setOperationType(OperationType.WRITE);
			msg.setEndPointId(EndpointType.LOG);	
			msg.setIsDirectInsert(true);
		//	appConfig.getQueueDirtyMsg().put(msg);						
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	
	@RequestMapping(value = "nav/err", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNavErr(@RequestBody ErrMsg msg) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("nav err ::::: " +msg);			
			//appConfig.getQueueDirtyMsg().put(msg);						
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav2/uni/mistake", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
	public StatusMsg jsonNavErr2(@RequestBody ErrMsg msg) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("nav mistake ::::: " +msg);			
			//appConfig.getQueueDirtyMsg().put(msg);						
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	

	
	
	
	@RequestMapping(value = "json/sub/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonConfirm(@RequestBody StatusMsg status) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			status.setMethodHandler("/async/json/sub/confirm");						
			status.setChannel(appConfig.getChannelBusBridge());
			status.setLogClass(BusSubMsg.class);
			status.setOperationType(OperationType.SUB);		
			status.setEventId(EventType.CONFIRM);
			appConfig.getQueueDirtyMsg().put(status);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
			
	//*only for testing
	//**	
	@RequestMapping(value = "json/pub/sorder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonSorderPub(@RequestBody SorderMsg sorder) {		
		try {																										
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());					
			sorder.setMethodHandler("/async/pub/json/sorder");			
			sorder.setChannel(appConfig.getChannelBusBridge());
			sorder.setEventId(EventType.CHANGE_CUST);
			sorder.setLogClass(BusPubMsg.class);
			sorder.setOperationType(OperationType.PUB);									
			appConfig.getQueueDirtyMsg().put(sorder);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav1/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav1Listener(@RequestBody SorderMsg sorder) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			System.out.println("client catch ");
			sorder.setMethodHandler("nav1/listener");			
			sorder.setChannel(appConfig.getChannelBusBridge());
			sorder.setLogClass(BusWriteMsg.class);			
			sorder.setOperationType(OperationType.WRITE);
			sorder.setEndPointId(EndpointType.NAV);			
			appConfig.getQueueDirtyMsg().put(sorder);				
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "nav2/listener", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonNav2Listener(@RequestBody SorderMsg sorder) {		
		try {				
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
			sorder.setMethodHandler("nav2/listener");			
			sorder.setChannel(appConfig.getChannelBusBridge());
			sorder.setLogClass(BusWriteMsg.class);			
			sorder.setOperationType(OperationType.WRITE);
			sorder.setEndPointId(EndpointType.ONE);			
			appConfig.getQueueDirtyMsg().put(sorder);				
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
