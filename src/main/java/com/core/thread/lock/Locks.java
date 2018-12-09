package com.core.thread.lock;

public class Locks {
    boolean locked = Boolean.FALSE;
    public synchronized void lock() throws InterruptedException{
        while(locked){
            wait();
        }
        locked = Boolean.TRUE;
    }

    public synchronized void unlock(){
        locked = Boolean.FALSE;
        notify();
    }
}