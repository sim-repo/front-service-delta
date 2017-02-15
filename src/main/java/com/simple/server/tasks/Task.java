package com.simple.server.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.simple.server.lifecycle.BasePhaser;
import com.simple.server.mediators.CommandType;
import com.simple.server.mediators.Subscriber;
import com.simple.server.statistics.Statistic;

public interface Task {
	 	void task() throws Exception;
	 	void setExecutor(ExecutorService executor);
	 	void setReentrantLock(ReentrantLock lock);
	 	void setCondition(Condition wakeup);
	    void setStatistic(Statistic statistic);
	    void setPhase(BasePhaser basePhaser);
	    void setOnAfterTaskDone(CommandType onAfterTaskDone);
	    void setOnBeforeStartTask(CommandType onBeforeStartTask);
	    void setOnRuntimeError(CommandType onRuntimeError);
	    void setDeactivateMySelfAfterTaskDone(Boolean deactivateMySelfAfterTaskDone);
	    void setSubscriber(Subscriber subscriber);
}
