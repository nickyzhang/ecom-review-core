package com.core.thread.lock;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock {
    // 持有锁的线程拥有者，如果为空表示锁未被占用
    AtomicReference<Thread> owner = new AtomicReference<>();

    public void lock() {
        // 获取当前线程
        Thread current = Thread.currentThread();
        // 如果锁没有被占用，则将当前线程设置为锁的拥有者
        while (owner.compareAndSet(null,current)) {

        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        // 如果锁的持有者是当前线程，则将持有者置空
        owner.compareAndSet(current,null);
    }
}
