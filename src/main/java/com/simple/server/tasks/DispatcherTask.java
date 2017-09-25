package com.simple.server.tasks;

import com.simple.server.config.AppConfig;
import com.simple.server.domain.contract.AContract;
import com.simple.server.domain.contract.ALogContract;
import com.simple.server.domain.contract.IContract;
import com.simple.server.mediators.CommandType;
import com.simple.server.statistics.time.Timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@SuppressWarnings("static-access")
@Service("DispatcherTask")
@Scope("prototype")
public class DispatcherTask extends AbstractTask {

	public static volatile AtomicInteger count = new AtomicInteger();
	
	@Autowired
	private AppConfig appConfig;
	
    private final static Integer MAX_NUM_ELEMENTS = 10000;
    private List<IContract> list = new ArrayList<IContract>();

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null && arg.getClass() == CommandType.class) {
            switch ((CommandType) arg) {
                case WAKEUP_CONSUMER:
                case WAKEUP_ALL:
                    arg = CommandType.WAKEUP_ALLOW;
                    super.update(o, arg);
                    break;
                case AWAIT_CONSUMER:
                case AWAIT_ALL:
                    arg = CommandType.AWAIT_ALLOW;
                    super.update(o, arg);
                    break;
            }
        }
    }

    
	@Override
    public void task() throws Exception {
        if (appConfig.getQueueDirtyMsg().drainTo(list, MAX_NUM_ELEMENTS) == 0) {
            list.add(appConfig.getQueueDirtyMsg().take());
        }
       
        Thread.currentThread().sleep(Timing.getTimeMaxSleep());	
        appConfig.getQueueDirtyMsg().drainTo(list, MAX_NUM_ELEMENTS);               
        for(IContract msg: list) {     
        	if(msg instanceof AContract){        	        
        		appConfig.getQueueClientMsg().put(msg);
        	}
        	else if(msg instanceof ALogContract){        	        		        			
        		appConfig.getQueueAdminMsg().put(msg);
        	}      		        			        			        	
        }
        list.clear();
    }

}