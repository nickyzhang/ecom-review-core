package com.core.thread.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NumCondition {

    public static void main(String[] args) {
        NumResource num = new NumResource(100,70);
        MinusTask minusTask = new MinusTask(num);
        AddTask addTask = new AddTask(num);
        int i = 0;
        int j = 0;
    }

}
