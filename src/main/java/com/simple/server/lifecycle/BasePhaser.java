package com.simple.server.lifecycle;


import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.simple.server.factory.TaskRunner;
import com.simple.server.mediators.CommandType;
import com.simple.server.statistics.time.Timing;
import com.simple.server.tasks.AbstractTask;
import com.simple.server.tasks.Task;

@Service("baseRunner")
@Scope("singleton")
public class BasePhaser extends AbstractTask {

	@Autowired
	TaskRunner taskRunner;
	
	
    protected static Phaser phaser;
    protected static final AtomicInteger currNumPhase = new AtomicInteger();
    protected int lastStep;

    public BasePhaser() {        
    }

    public void setLastStep(int lastStep){
        this.lastStep = lastStep;
    }

    public static Phaser getPhaser() {
        return phaser;
    }

    public static Integer getCurrNumPhase(){
        return currNumPhase.get();
    }

    public void nextPhase(){
        phaser.arrive();
        currNumPhase.getAndIncrement();
        super.setChanged();
        super.notifyObservers(currNumPhase.get());
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null && arg.getClass() == CommandType.class) {
            switch ((CommandType) arg) {
                case WAKEUP_PHASER:
                case WAKEUP_ALL:
                    super.update(o, CommandType.WAKEUP_ALLOW);
                    break;
                case AWAIT_PHASER:
                case AWAIT_ALL:
                    super.update(o, CommandType.AWAIT_ALLOW);
                    break;
            }
        }
    }

    @Override
    public void task() throws Exception {
        if(lastStep <= 0)
            throw  new Exception(String.format("Phaser %s must be initialized with last step gt 0",this));
        phaser = new Phaser(1);
        currNumPhase.set(0);
        Thread.currentThread().sleep(Timing.getTimeMaxSleep());
        nextPhase();

        Thread.currentThread().sleep(Timing.getTimeMaxSleep());

        for (int i = currNumPhase.get(); i < lastStep; i++) {
            nextPhase();
            Thread.currentThread().sleep(Timing.getTimeMaxSleep());
        }
        phaser.arriveAndDeregister();
        phaser = null;
    }

    public void addPhaseToTask(Class clazz){
        List<Task> tasks = taskRunner.getTasksByClass(clazz);
        for(Task task: tasks){
            task.setPhase(this);
        }
    }
}
