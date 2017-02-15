package com.simple.server.statistics.time;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.simple.server.statistics.Statistic;

public class Timing extends TimerTask {
    public static final Long TIME_MAX_SLEEP = 1l;
    private List<Statistic> statistics = new ArrayList<>();

    public Timing(Statistic statistic) {
        addStatToTiming(statistic);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, 0, 1000);
    }

    public Timing(List<Statistic> statistics) {
        addStatsToTiming(statistics);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, 0, 1000);
    }

    public static Long getTimeMaxSleep() {
        return TIME_MAX_SLEEP;
    }

    public void addStatToTiming(Statistic statistic){
        statistics.add(statistic);
    }

    public void addStatsToTiming(List<Statistic> statistics){
        this.statistics.addAll(statistics);
    }

    @Override
    public void run() {
        saveAll();
    }

    private void saveAll() {
        for(Statistic statistic: statistics){
            statistic.save();
        }
    }
}
