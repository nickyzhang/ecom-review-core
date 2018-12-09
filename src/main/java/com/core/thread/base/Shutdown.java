package com.core.thread.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Shutdown {
    public static void main(String[] args) {
        Runner runner = new Runner();
    }

    static class Runner implements Runnable {
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(Thread.currentThread().getName() + " Run at " +
                        format.format(new Date()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
