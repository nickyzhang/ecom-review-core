package com.core.thread.base;

import java.util.concurrent.locks.ReentrantLock;

public class Interrupted {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runner1());
        Thread t2 = new Thread(new Runner1());
        t1.start();
        try {
            t1.sleep(5000);
            Thread.interrupted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }


    static class Runner1 implements Runnable{

        public void run() {

        }
    }

    static class Runner2 implements Runnable{

        public void run() {

        }
    }


}
