package com.simple.server.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.simple.server.config.AppConfig;
import com.simple.server.mediators.CommandType;
import com.simple.server.mediators.Mediator;
import com.simple.server.mediators.ParameterType;
import com.simple.server.tasks.AbstractTask;
import com.simple.server.tasks.DispatcherTask;
import com.simple.server.tasks.LoadConfigTask;
import com.simple.server.tasks.ClientSenderTask;
import com.simple.server.tasks.LogSenderTask;
import com.simple.server.tasks.Task;

@Service("taskRunner")
@Scope("singleton")
public class TaskRunner  {

	@Autowired
	private ApplicationContext appContext;
	
	
	@Autowired
	private AppConfig appConfig;
		
    CopyOnWriteArrayList<ExecutorService> executors = new CopyOnWriteArrayList<>();
    ConcurrentHashMap<Object, List<Task>> tasks = new ConcurrentHashMap<>();
 
    

	public <T extends AbstractTask> List<T> newRunTask(Mediator mediator, Class<T> clazz, int num) throws Exception{
    	 ReentrantLock lock = new ReentrantLock();
         Condition condition = lock.newCondition();
         ExecutorService executor = Executors.newFixedThreadPool(num);
         List<T> list = new ArrayList<>();
         for (int i =0; i < num; i++) {
        	 T t =  (T)appContext.getBean(clazz.getSimpleName());
        	 t.setExecutor(executor);
        	 t.setReentrantLock(lock);
        	 t.setCondition(condition);        
        	 mediator.addObserver(t);
        	 t.addObserver(mediator);
        	 executor.submit(t);
        	 addTask(t.getClass(), t);
        	 list.add(t);
         }         
         executors.add(executor);         
         return list;
    }
    


    private void addTask(Object o, Task t){
        List<Task> list = null;
        if(tasks.containsKey(o)){
            list = tasks.get(o);
        }else{
            list = new ArrayList<>();
        }
        list.add(t);
        tasks.put(o,list);
    }

    
    
    public List<Task> getTasksByClass(Class clazz) {
        if(tasks.containsKey(clazz))
            return tasks.get(clazz);
        return null;
    }

    public void setParam(ParameterType parameterType, Class clazz, Object object){
        if(tasks.containsKey(clazz)){
            List<Task> taskList = tasks.get(clazz);
            for(Task task: taskList){
                switch (parameterType) {
                    case ON_BEFORE_START_TASK: task.setOnBeforeStartTask((CommandType)object); break;
                    case ON_AFTER_TASK_DONE: task.setOnAfterTaskDone((CommandType)object); break;
                    case DEACTIVATE_AFTER_TASK_DONE: task.setDeactivateMySelfAfterTaskDone((Boolean)object); break;
                    case ON_RUNTIME_ERROR: task.setOnRuntimeError((CommandType)object); break;
                }
            }
        }
    }
     
    public void initProcessing() {        
        	try {		    		            	
        		newRunTask(appConfig.getMediator(), DispatcherTask.class, 2);
        		newRunTask(appConfig.getMediator(), ClientSenderTask.class, 2);  
        		newRunTask(appConfig.getMediator(), LogSenderTask.class, 2);
        		newRunTask(appConfig.getMediator(), LoadConfigTask.class, 1);
        		//appConfig.getPerfomancerStat().addStatToTask(Processing.class);
                // Timing timing = new Timing(appConfig.getPerfomancerStat());        		         		
            	appConfig.getMediator().wakeupAll();
        	} catch (Exception e) {
        
    			e.printStackTrace();
    		}           		
    }

    
    
    
}
