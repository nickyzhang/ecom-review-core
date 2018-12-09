package com.core.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NumResource {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    int num,min;

    public NumResource(int num, int min) {
        this.num = num;
        this.min = min;
    }

    public void decr() {
        lock.lock();
        try {
            while (num < min) {
                System.out.println(Thread.currentThread().getName()+" >> 不能低于70，请等待......");
                condition.await();
            }
            --num;
            System.out.println(Thread.currentThread().getName()+" [minus]现在num值: "+num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void incr() {
        lock.lock();
        try {
            ++num;
            System.out.println(Thread.currentThread().getName()+" [add]现在num值: "+num);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
