package com.simple.server.tasks;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.AContract;
import com.simple.server.domain.contract.IContract;
import com.simple.server.mediators.CommandType;
import com.simple.server.statistics.time.Timing;

@SuppressWarnings("static-access")
@Service("ClientSenderTask")
@Scope("prototype")
public class ClientSenderTask extends AbstractTask {
		
	@Autowired
	private AppConfig appConfig;
	
	private static Integer MAX_NUM_ELEMENTS = 1000;
    private List<IContract> list = new ArrayList<>();

    @Override
    public void update(Observable o, Object arg) {

        if(arg.getClass().equals(CommandType.class)) {
            switch ((CommandType) arg) {
                case WAKEUP_PROCESSING:
                case WAKEUP_ALL:
                    super.update(o, CommandType.WAKEUP_ALLOW);
                    break;
                case AWAIT_PROCESSING:
                case AWAIT_ALL:
                    super.update(o, CommandType.AWAIT_ALLOW);
                    break;
            }
        }      
    }


    @Override
    public void task() throws Exception {  
        if(appConfig.getQueueClientMsg().drainTo(list, MAX_NUM_ELEMENTS)==0){
            list.add(appConfig.getQueueClientMsg().take());
        }
             
        Thread.currentThread().sleep(Timing.getTimeMaxSleep());		
        appConfig.getQueueClientMsg().drainTo(list, MAX_NUM_ELEMENTS);  
  
        for (IContract msg : list) {        	
        	Thread.currentThread().sleep(Timing.getTimeMaxSleep());		   
        	msg.setRequestInDatetime(new SimpleDateFormat(AppConfig.DATEFORMAT).format(Calendar.getInstance().getTime()));        	
        	msg.setRequestInDatetime(new SimpleDateFormat(AppConfig.DATEFORMAT).format(Calendar.getInstance().getTime()) );        	
        	appConfig.getLogBusMsgService().transformAndSend(appConfig.getChannelBusLog(), (AContract)msg);          	
        	appConfig.getBusMsgService().send(msg.getChannel(), msg.getMethodHandler(), msg);		
        }
                   
        throwToStatistic(list.size());
        list.clear();
    }
    
}