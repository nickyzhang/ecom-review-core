package com.core.thread.base;

public class SubThread1 extends Thread {
    private int i;
    @Override
    public void run() {
        for (; i < 100; i++) {
            System.out.println(getName()+" "+i);
        }
    }
}
