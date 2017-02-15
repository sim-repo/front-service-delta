package com.simple.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.simple.server.factory.TaskRunner;


@Component
public class PostInit implements ApplicationListener<ContextRefreshedEvent> {
	  
	@Autowired
	  TaskRunner taskRunner;
	  
	@Autowired
	AppConfig appConfig;	  
	  
	@Value("${queueSizeAdminMsg.int.property :100}")
	private int initQueueSizeAdminMsg;
	
	@Value("${queueSizeClientMsg.int.property :100}")
	private int initQueueSizeClientMsg;
	  
	@Value("${queueSizeDirtyMsg.int.property :100}")
	private int initQueueSizeDirtyMsg;
	   
	@Value("${queueSizeDirtyPlainText.int.property :100}")
	private int initQueueSizeDirtyPlainText;
	

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
					
		appConfig.initQueueAdminMsg(initQueueSizeAdminMsg);
		appConfig.initQueueClientMsg(initQueueSizeClientMsg);
		appConfig.initQueueDirtyMsg(initQueueSizeDirtyMsg);
		appConfig.initQueueDirtyPlainText(initQueueSizeDirtyPlainText);
		
		taskRunner.initProcessing();
	}
}