package com.simple.server.statistics;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.simple.server.factory.TaskRunner;
import com.simple.server.tasks.Task;


@Service("perfomancerStat")
@Scope("singleton")
public class PerfomancerStat implements Statistic {
	@Autowired
	TaskRunner taskRunner;
	
    Long startTime;
    Long durationSum;
    Long measurementQty = 0l;
    Long unitTotalSum = 0l;
    Long unitSumPerTimeInterval = 0l;

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        durationSum += endTime-startTime;
        measurementQty++;
    }

    public void setMeasurementQty(Long measurementQty) {
        this.measurementQty = measurementQty;
    }

    public Long getAvgDuration(){
        return durationSum/measurementQty;
    }

    public void setCurrUnitSum(int currUnitSum) {
        this.unitSumPerTimeInterval += currUnitSum;
    }

    public void save(){
        unitTotalSum += unitSumPerTimeInterval;
        unitSumPerTimeInterval = 0l;
        measurementQty++;

        System.out.println("stat:"+(unitTotalSum/measurementQty));
    }

    public void addStatToTask(Class clazz){
        List<Task> tasks = taskRunner.getTasksByClass(clazz);
        for(Task task: tasks){
            task.setStatistic(this);
        }
    }
}
