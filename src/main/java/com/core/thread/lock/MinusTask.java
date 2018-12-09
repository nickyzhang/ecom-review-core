package com.core.thread.lock;

public class MinusTask implements Runnable {
    private NumResource num;

    public MinusTask(NumResource num) {
        this.num = num;
    }

    @Override
    public void run() {
        this.num.decr();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
