package com.simple.server.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.config.OperationType;
import com.simple.server.domain.contract.BusPubMsg;
import com.simple.server.domain.contract.BusSubMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.domain.contract.UniMsg;
import com.simple.server.statistics.time.Timing;
import com.simple.server.util.ObjectConverter;


@SuppressWarnings("static-access")
@RestController
@RequestMapping("/async")
public class AsyncPubController {

	@Autowired
	private AppConfig appConfig;
	
	
	@RequestMapping(value = "json/pub/uni", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> jsonPub(@RequestBody UniMsg msg) {
		HttpHeaders headers = new HttpHeaders();		
		try {					
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			msg.setMethodHandler("/async/json/pub/uni");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusPubMsg.class);
			msg.setOperationType(OperationType.PUB);
		
			appConfig.getQueueDirtyMsg().put(msg);
			return new ResponseEntity<String>("", headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getCause().toString(), headers, HttpStatus.BAD_REQUEST);
		}
	}
	

	@RequestMapping(value = "xml/pub/uni", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> xmlPub(HttpServletRequest request, @RequestBody String xml) {
		HttpHeaders headers = new HttpHeaders();		
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());							
			
			UniMsg msg = (UniMsg)ObjectConverter.xmlToObject(xml, UniMsg.class);										
			msg.setMethodHandler("/async/json/pub/uni");
			msg.setChannel(appConfig.getChannelBusBridge());
			msg.setLogClass(BusPubMsg.class);
			msg.setOperationType(OperationType.PUB);
			
			appConfig.getQueueDirtyMsg().put(msg);
			return new ResponseEntity<String>("", headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getCause().toString(), headers, HttpStatus.BAD_REQUEST);
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
		
			
			appConfig.getQueueDirtyMsg().put(status);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	
	
	@RequestMapping(value = "xml/sub/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public StatusMsg xmlConfirm(@RequestBody StatusMsg status) {
		try {
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());
			status.setMethodHandler("/async/xml/sub/confirm");
			status.setChannel(appConfig.getChannelBusBridge());
			status.setLogClass(BusSubMsg.class);
			status.setOperationType(OperationType.SUB);
		
			
			appConfig.getQueueDirtyMsg().put(status);
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}

}
