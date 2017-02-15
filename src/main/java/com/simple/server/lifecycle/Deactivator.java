package com.simple.server.lifecycle;

import java.util.concurrent.Phaser;

public class Deactivator extends Phaser {
    private final Object target;

    public Deactivator(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }
}
