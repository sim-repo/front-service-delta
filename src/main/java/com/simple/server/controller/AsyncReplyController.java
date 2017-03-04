package com.simple.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.SorderMsg;
import com.simple.server.domain.contract.StatusMsg;
import com.simple.server.statistics.time.Timing;

@RestController
@RequestMapping("/async/reply/")
public class AsyncReplyController {
	
	@Autowired
	private AppConfig appConfig;
	
	@RequestMapping(value = "json/so", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StatusMsg jsonSoReply(@RequestBody SorderMsg so) {		
		try {																					
					
			Thread.currentThread().sleep(Timing.getTimeMaxSleep());			
			if(so.getJuuid()==null)
				throw new Exception("Exception: juuid is required for replying.'");
										
			so.setMethodHandler("/async/reply/json/so");
			so.setChannel(appConfig.getChannelBusBridge());			
			appConfig.getQueueDirtyMsg().put(so);					
			return appConfig.getSuccessStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return new StatusMsg("406", e.toString());
		}
	}
	

}
