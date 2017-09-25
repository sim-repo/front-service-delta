package com.simple.server.mediators;

public enum CommandType {
    WAKEUP_ALL,
    AWAIT_ALL,
    WAKEUP_PRODUCER,
    AWAIT_PRODUCER,
    WAKEUP_CONSUMER,
    AWAIT_CONSUMER,
    WAKEUP_PROCESSING,
    AWAIT_PROCESSING,
    WAKEUP_PHASER,
    AWAIT_PHASER,
    PRODUCER_TASK_DONE,
    TASK1_DONE,
    TASK2_DONE,
    TASK3_DONE,
    TASK1_ERROR,
    TASK2_ERROR,
    WAKEUP_ALLOW,
    AWAIT_ALLOW
}
