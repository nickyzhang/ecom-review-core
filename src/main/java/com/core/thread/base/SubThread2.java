package com.core.thread.base;

public class SubThread2 implements Runnable {
    private int i;
    public void run() {
        for (; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
    }
}
