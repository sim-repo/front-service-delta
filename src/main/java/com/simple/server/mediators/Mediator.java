package com.simple.server.mediators;


import java.util.*;

import com.simple.server.tasks.states.State;


public class Mediator extends Observable implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && !arg.getClass().equals(State.class)) {
            setChanged();
            notifyObservers(arg);
        }
        else
        if (arg instanceof State) {
            System.out.println("Mediator:"+o.getClass()+":"+((State) arg).getMessage());
        }
    }
    
    public void wakeupAll(){
    	setChanged();
    	notifyObservers(CommandType.WAKEUP_ALL);
    }
}
