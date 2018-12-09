package com.core.thread.base;

public class JoinCase implements Runnable {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JoinCase join = new JoinCase();
        Thread[] threads = new Thread[10];
        for(int i = 0; i < threads.length; i++) {
            if (i == 2) {
                threads[i] = new Thread(join);
                threads[i].start();
//                Thread.currentThread().interrupt();
                threads[i].interrupt();
            }
            System.out.println(Thread.currentThread().getName()+" => "+i);
        }

    }
}
