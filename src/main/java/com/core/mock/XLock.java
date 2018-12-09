package com.core.mock;

import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class XLock implements Lock, java.io.Serializable {

    private static final long serialVersionUID = 7373984872572414699L;

    public final Sync sync; // 管理锁的机制

    // 对锁进行同步控制管理的一个内部类，主要包括公平和非公平两种类型
    public abstract static class Sync extends AbstractSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;
        // 加锁
        abstract void lock();

        // 尝试获取非公平锁
        final boolean nonfairTryAcquire(int acquires) {
            // 获取当前线程
            final Thread current = Thread.currentThread();
            // 获取当前锁状态
            int c = getState();
            // 获取当前锁没有被抢占，然后尝试更改锁的状态，更改成功，则当前线程获取锁
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } // 如果当前锁已经占用则增加传递的
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

        // 尝试释放锁,设置锁状态;只有锁没有被其他线程持有的情况下才会释放
        protected final boolean tryRelease(int releases) {
            // 重新计算状态
            int c = getState() - releases;
            // 持有锁的线程不是当前线程，直接抛出异常
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) { // 如果没有嵌套锁(重入)，可以释放了
                free = true; // 
                setExclusiveOwnerThread(null);// 将当前持有独占锁的线程置为null
            }
            setState(c); // 重新设置状态
            return free;
        }

        // 检查当前线程是否获取了独占锁，未获取独占锁调用 signal 方法是不允许的
        protected final boolean isHeldExclusively() {

            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        // 构建Condition
        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // 获取持有当前锁的线程
        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        // 当前锁是否被占用
        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    // 非公平锁的实现
    public static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        final void lock() {
            // 如果当前锁的状态不是0，则该线程等待；如果当前锁没有被占用，则标记为已经被占用
            if (compareAndSetState(0, 1))
                // 将当前线程设置为锁独占线程
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
        // 尝试非公平获取锁
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }


    // 公平锁
    public static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;
        // 获取锁
        final void lock() {
            acquire(1);
        }

        // 尝试获取公平锁，如果没有被占用，或者虽然被占用但是是当前线程持有(可重入)都可以返回true
        protected final boolean tryAcquire(int acquires) {
            // 当前线程
            final Thread current = Thread.currentThread();
            // 获取锁状态
            int c = getState();
            // 如果锁没有被占用
            if (c == 0) {
                // 查看是否有其他线程等待获取锁，如果没有
                // 则尝试更改状态，标记这所被占，如果没有问题，则将
                // 独占锁线程设置为当前线程
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } // 如果已经被占用，即独占锁线程已经是当前线程
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires; // 自增获取锁的次数
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc); // 设置状态
                return true;
            }
            return false;
        }
    }

    // 构造ReentrantLock，默认是不公平的锁
    public XLock() {
        sync = new NonfairSync();
    }

    public XLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
    // 抢占锁
    public void lock() {
        sync.lock();
    }

    // 抢占锁,如果中断则退出抢占
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    // 尝试获取非公平锁
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    // 尝试获取带有超时机制的锁
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    // 释放锁,持有锁的数量递减
    public void unlock() {
        sync.release(1);
    }

    // 返回一个Condition对象
    public Condition newCondition() {
        return sync.newCondition();
    }


    public int getHoldCount() {
        return sync.getHoldCount();
    }

    // 锁是不是被当前线程持有
    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }

    // 锁是否被抢占了
    public boolean isLocked() {
        return sync.isLocked();
    }

    // 是不是公平锁
    public final boolean isFair() {
        return sync instanceof FairSync;
    }

    // 获取当前持有锁的线程
    protected Thread getOwner() {
        return sync.getOwner();
    }

    // 是否还有其他线程正在等待获取锁
    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    // 正在等待获取锁的线程是否包括当前线程
    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }

    // 获取度列长度
    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    // 获取队列所有线程
    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }


    // 查询是否有线程正在等待与此锁相关联的给定条件
    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractSynchronizer.ConditionObject)condition);
    }

    // 获取条件等待队列
    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractSynchronizer.ConditionObject)condition);
    }

    // 获取和条件关联的等待线程
    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractSynchronizer.ConditionObject)condition);
    }

    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }

}
