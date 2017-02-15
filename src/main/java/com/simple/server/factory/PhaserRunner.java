package com.simple.server.factory;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.simple.server.lifecycle.BasePhaser;
import com.simple.server.mediators.Mediator;


@Service("phaserRunner")
@Scope("singleton")
public class PhaserRunner {
	@Autowired
	TaskRunner taskRunner;
	
    public <T extends BasePhaser> T newRunPhaser(Mediator mediator, Class<T> clazz, int lastStep) throws Exception {
        ArrayList<T> list = (ArrayList<T>)taskRunner.newRunTask(mediator, clazz, 1);
        list.get(0).setLastStep(lastStep);
        return list.get(0);
    }
}