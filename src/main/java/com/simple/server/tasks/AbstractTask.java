package com.simple.server.tasks;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.simple.server.lifecycle.BasePhaser;
import com.simple.server.lifecycle.Deactivator;
import com.simple.server.mediators.CommandType;
import com.simple.server.mediators.Mediator;
import com.simple.server.mediators.Subscriber;
import com.simple.server.statistics.Statistic;
import com.simple.server.statistics.time.Timing;
import com.simple.server.tasks.states.State;


public abstract class AbstractTask extends Observable implements Task, Callable, Observer {    
	private ExecutorService executor;
    protected ReentrantLock lock;
    protected Condition wakeup;
    private Boolean isActive = false;
    private Statistic statistic;
    private static List<Deactivator> deactivators = new ArrayList();
    private Boolean deactivateAfterTaskDone = false;
    protected Subscriber subscriber;
    protected CommandType onBeforeStartTask;
    protected CommandType onAfterTaskDone;
    protected CommandType onRuntimeError;
    protected BasePhaser basePhaser;
    
    
    @Override
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;		
	}

	@Override
	public void setReentrantLock(ReentrantLock lock) {
		this.lock = lock;		
	}

	@Override
	public void setCondition(Condition wakeup) {
		this.wakeup = wakeup;
	}

	public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean flag) {
        isActive = flag;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public void throwToStatistic(int qty){
        if (statistic != null)
            statistic.setCurrUnitSum(qty);
    }

    public void setPhase(BasePhaser basePhaser) {
        this.basePhaser = basePhaser;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public CommandType getOnBeforeStartTask() {
        return onBeforeStartTask;
    }

    public void setOnBeforeStartTask(CommandType onBeforeStartTask) {
        this.onBeforeStartTask = onBeforeStartTask;
    }

    public CommandType getOnAfterTaskDone() {
        return onAfterTaskDone;
    }

    public void setOnAfterTaskDone(CommandType onAfterTaskDone) {
        this.onAfterTaskDone = onAfterTaskDone;
    }

    public CommandType getOnRuntimeError() {
        return onRuntimeError;
    }

    public void setOnRuntimeError(CommandType onRuntimeError) {
        this.onRuntimeError = onRuntimeError;
    }        
    

	@Override
    public void update(Observable o, Object arg){
        try {
            if (!(o instanceof Mediator)) {
                throw new Exception("Observable object must be as instance of Dispatcher");
            }

            if(arg != null){
                if(!(arg instanceof CommandType)){
                    throw new Exception("arg must be as CommandType enum");
                }
            }

            lock.lock();
            if (arg!=null) {
                switch ((CommandType)arg) {
                    case WAKEUP_ALLOW:
                    case WAKEUP_ALL: {
                            setIsActive(true);
                            wakeup.signal();
                        break;
                    }
                    case AWAIT_ALL:
                    case AWAIT_ALLOW: {
                        setIsActive(false);
                        wakeup.signal();
                        break;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    

    public static void setDeactivators(List<Deactivator> deactivators) {
        AbstractTask.deactivators = deactivators;
        for(Deactivator deactivator: deactivators){
            deactivator.register();
        }
    }

    public void setDeactivateMySelfAfterTaskDone(Boolean deactivateAfterTaskDone) {
        this.deactivateAfterTaskDone = deactivateAfterTaskDone;
    }

    @Override
    public Object call() throws Exception {
        System.out.println(this);
        
        while(!executor.isShutdown()){
            try {            	
                lock.lock();                
                while (!getIsActive()){            
                    wakeup.await();     
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

            try{
                Thread.currentThread().sleep(Timing.getTimeMaxSleep());

                if(getOnBeforeStartTask() != null){
                    setChanged();
                    notifyObservers(getOnBeforeStartTask());
                }

                task();

                if(getOnAfterTaskDone() != null){
                    setChanged();
                    notifyObservers(getOnAfterTaskDone());
                }

                if(deactivateAfterTaskDone){
                    setIsActive(false);
                }
            }catch (Exception e){
                setChanged();
                notifyObservers(getOnRuntimeError());

                setChanged();
                State state = new State();
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                state.setMessage(errors.toString());
                notifyObservers(state);
            }
        }

        return null;
    }

    public abstract void task() throws Exception;
}
