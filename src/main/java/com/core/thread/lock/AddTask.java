package com.core.thread.lock;

public class AddTask implements Runnable{
    private NumResource num;

    public AddTask(NumResource num) {
        this.num = num;
    }

    @Override
    public void run() {
        this.num.incr();
    }
}
