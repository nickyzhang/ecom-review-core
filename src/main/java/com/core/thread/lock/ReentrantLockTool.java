package com.core.thread.lock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTool {
    Lock lock = new ReentrantLock(true);
    int count;
    public void inc() throws InterruptedException {
        lock.lock();
        count++;
        System.out.println(count);
        doInc();
        lock.unlock();
    }

    public void doInc(){
        lock.lock();
        count++;
        System.out.println(count);
        lock.unlock();
    }

    public static void main(String[] args) {
        int c = 1 << 16;
        System.out.println( c+ (1 << 16) == c * 2);
    }
}
