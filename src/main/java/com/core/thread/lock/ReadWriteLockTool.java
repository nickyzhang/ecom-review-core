package com.core.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTool {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public void read() {
        try {
            readLock.lock();
            System.out.println("当前读线程=> ["+Thread.currentThread().getName()+"] 进入");
//            Thread.sleep(3000);
            System.out.println("当前读线程=> ["+Thread.currentThread().getName()+"] 退出");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write() {
        try {
            writeLock.lock();
            System.out.println("当前写线程=> ["+Thread.currentThread().getName()+"] 进入");
            //Thread.sleep(3000);
            System.out.println("当前写线程=> ["+Thread.currentThread().getName()+"] 退出");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockTool rwlt = new ReadWriteLockTool();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                rwlt.read();
            }
        },"t1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                rwlt.write();
            }
        },"t2");
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                rwlt.read();
            }
        },"t3");

        t1.start();
        t2.start();
        t3.start();
    }
}
